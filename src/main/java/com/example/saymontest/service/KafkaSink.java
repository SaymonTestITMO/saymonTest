package com.example.saymontest.service;


import com.example.saymontest.config.KafkaProperties;
import com.example.saymontest.model.SinkMessageImpl;
import com.example.saymontest.service.interfaces.Sink;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaSink implements Sink {

    private final KafkaTemplate<String, SinkMessageImpl> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    @Override
    @Retry(name = "kafkaSender")
    @TimeLimiter(name = "kafkaSender")
    public void accept(SinkMessageImpl message) {
        kafkaTemplate.send(kafkaProperties.getSinkTopic(), message.getLabels().toString(), message);
    }
}
