package com.example.saymontest.pipeline;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.example.saymontest.model.SourceMessageImpl;
import com.example.saymontest.service.interfaces.Source;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ProcessorPipeline {

    private final Source source;
    private final Deduplicator deduplicator;
    private final Filter filter;
    private final Grouper grouper;
    private final Aggregator aggregator;

    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void start() {
        scheduledExecutorService.scheduleAtFixedRate(aggregator::flush, 10, 10, TimeUnit.SECONDS);

        for (SourceMessageImpl message : source.source()) {
            process(message);
        }
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdownNow();
        scheduledExecutorService.shutdownNow();
    }

    public void process(SourceMessageImpl message) {
        executorService
                .submit(() ->
                {
                    if (!deduplicator.isDuplicate(message) && filter.passes(message)) {
                        String groupKey = grouper.groupKey(message);
                        aggregator.add(groupKey, message);
                    }
                });
    }
}






















