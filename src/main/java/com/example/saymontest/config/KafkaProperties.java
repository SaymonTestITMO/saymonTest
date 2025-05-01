package com.example.saymontest.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    private String bootstrapServers;
    private String sourceTopic;
    private String sinkTopic;

    private RetryProperties consumerRetry = new RetryProperties();

    @Getter
    @Setter
    public static class RetryProperties {
        private int attempts;
        private long backoffMs;
    }
}
