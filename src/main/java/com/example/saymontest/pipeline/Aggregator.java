package com.example.saymontest.pipeline;

import com.example.saymontest.model.SinkMessageImpl;
import com.example.saymontest.model.SourceMessageImpl;
import com.example.saymontest.service.interfaces.Sink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@RequiredArgsConstructor
public class Aggregator {

    private final Sink sink;
    private final Map<String, Stats> state = new ConcurrentHashMap<>();

    public void add(String key, SourceMessageImpl message) {
        state.compute(key, (k, v) -> {
            if (v == null)
                v = new Stats(message.getTimeStamp());
            v.update(message.getValue(), message.getTimeStamp());
            return v;
        });
    }

    public void flush() {
        long now = Instant.now().toEpochMilli();
        state.forEach((key, stats) -> {
            SinkMessageImpl outMessage = stats.toSinkMessage(key, now);
            sink.accept(outMessage);
        });
        state.clear();
    }
}
