package com.example.saymontest.pipeline;

import com.example.saymontest.aspects.annotations.Metric;
import com.example.saymontest.config.PipelineProperties;
import com.example.saymontest.exception.DeduplicationException;
import com.example.saymontest.model.api.SourceMessage;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class Deduplicator {

    private final PipelineProperties pipelineProperties;
    private Cache<String, Boolean> cache;

    @PostConstruct
    public void init() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(pipelineProperties.getDuplicationWindowSeconds(), TimeUnit.SECONDS)
                .build();
    }

    @Metric(name = "deduplication", tags = {"stage=preprocessing"})
    public boolean isDuplicate(SourceMessage message) {
        try {
            String key = buildDeduplicationKey(message.labels());
            return cache.get(key, k -> false);
        } catch (DeduplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DeduplicationException("Unexpected Deduplication failed: " + e);
        }
    }

    private String buildDeduplicationKey(@NotNull Map<String, String> labels) {

        List<String> deduplicatoinKeysList = pipelineProperties.getDeduplicationKeys();
        if (deduplicatoinKeysList.isEmpty())
            throw new DeduplicationException("Deduplication keys aren`t configured");

        deduplicatoinKeysList.forEach(key -> {
            if (!labels.containsKey(key)) {
                throw new DeduplicationException("Missing deduplication key: " + key);
            }
        });

        return labels.entrySet().stream()
                .filter(entry -> deduplicatoinKeysList.contains(entry.getKey()))
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> String.join("=", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("|", "", "|" + propertiesHash()));
    }

    private int propertiesHash() {
        return pipelineProperties.getDeduplicationKeys().hashCode();
    }
}
