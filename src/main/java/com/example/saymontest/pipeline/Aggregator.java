package com.example.saymontest.pipeline;

import com.example.saymontest.aspects.annotations.Loggable;
import com.example.saymontest.aspects.annotations.Metric;
import com.example.saymontest.exception.AggregationException;
import com.example.saymontest.model.api.SinkMessage;
import com.example.saymontest.model.api.SourceMessage;
import com.example.saymontest.service.interfaces.Sink;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Component
@Slf4j
@RequiredArgsConstructor
public class Aggregator {

    private final Sink sink;
    private final Map<String, Stats> state = new ConcurrentHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Metric(name = "aggregation.add", tags = {"stage=processing"})
    public void add(String key, SourceMessage message) {
        try {
            lock.readLock().lock();
            try {
                state.compute(key, (k, v) -> {
                    if (v == null)
                        v = new Stats(message.timestamp());
                    v.update(message.value(), message.timestamp());
                    return v;
                });
            } finally {
                lock.readLock().unlock();
            }
        } catch (ArithmeticException e) {
            throw new AggregationException("Aggregation error for key " + key);
        }
    }

    @Loggable
    @Metric(name = "aggregation.flush", tags = {"stage=postprocessing"})
    public void flush() {
        lock.writeLock().lock();
        try {
            long now = Instant.now().toEpochMilli();
            state.forEach((key, stats) -> {
                try {
                    SinkMessage outMessage = stats.toSinkMessage(key, now);
                    sink.accept(outMessage);
                } catch (Exception e) {
                    log.error("Failed to send message to Sink: {}", e.getMessage());
                    throw new AggregationException("Failed to send to Sink: " + e.getMessage());
                }
            });
            state.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
