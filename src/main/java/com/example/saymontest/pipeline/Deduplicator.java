package com.example.saymontest.pipeline;

import com.example.saymontest.config.PipelineProperties;
import com.example.saymontest.model.SourceMessageImpl;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

    public boolean isDuplicate(SourceMessageImpl message) {
        Objects.requireNonNull(message, "Message cannot be null");
        String key = buildDeduplicationKey(message.getLabels());
        return cache.get(key, k -> false);
    }

    private String buildDeduplicationKey(@NotNull Map<String, String> labels) {
        return labels.entrySet()
                .stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> String.join("=", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("|", "", "|" + propertiesHash()));
    }

    private int propertiesHash() {
        return pipelineProperties.getDeduplicationKeys().hashCode();
    }
}
