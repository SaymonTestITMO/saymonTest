package com.example.saymontest.pipeline;

import com.example.saymontest.aspects.annotations.Loggable;
import com.example.saymontest.aspects.annotations.Metric;
import com.example.saymontest.config.PipelineProperties;
import com.example.saymontest.model.api.SourceMessage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.example.saymontest.service.interfaces.Source;

import java.util.concurrent.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProcessorPipeline {

    private static final int FLUSH_INTERVAL_SEC = 10;
    private final PipelineProperties pipelineProperties;
    private final BlockingQueue<Runnable> taskQueue =
            new LinkedBlockingQueue<>(1000);
    private final Source source;
    private final Deduplicator deduplicator;
    private final Filter filter;
    private final Grouper grouper;
    private final Aggregator aggregator;

    private final ExecutorService executorService =
            new ThreadPoolExecutor(
                    pipelineProperties.getThreadPoolSize(),
                    pipelineProperties.getThreadPoolSize(),
                    0L, TimeUnit.MILLISECONDS,
                    taskQueue
            );
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void start() {
        scheduledExecutorService.scheduleAtFixedRate(aggregator::flush, FLUSH_INTERVAL_SEC, FLUSH_INTERVAL_SEC, TimeUnit.SECONDS);

        for (SourceMessage message : source.source()) {
            process(message);
        }
    }

    @PreDestroy
    @Metric(name = "pipeline.shutdown", tags = {"stage=control"})
    public void shutdown() {
        executorService.shutdownNow();
        scheduledExecutorService.shutdownNow();
    }

    @Loggable
    @Metric
    public void process(SourceMessage message) {
        executorService
                .submit(() ->
                {
                    try {
                        if (!deduplicator.isDuplicate(message) && filter.passes(message)) {
                            String groupKey = grouper.groupKey(message);
                            aggregator.add(groupKey, message);
                        }
                    } catch (Exception e) {
                        log.error("Pipeline processing error: {}", e.getMessage());
                    }
                });
    }
}