package com.example.saymontest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "pipeline")
public class PipelineProperties {
    private List<String> deduplicationKeys;
    private int duplicationWindowSeconds;
    private String filterRule;
    private List<String> groupByKeys;
    private int windowSizeSeconds;
}
