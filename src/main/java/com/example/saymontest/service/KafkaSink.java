package com.example.saymontest.service;


import com.example.saymontest.aspects.annotations.Metric;
import com.example.saymontest.config.KafkaProperties;
import com.example.saymontest.model.SinkMessageImpl;
import com.example.saymontest.model.api.SinkMessage;
import com.example.saymontest.service.interfaces.Sink;
import com.example.saymontest.service.interfaces.SinkPayloadFactory;
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
    private final SinkPayloadFactory payloadFactory;

    @Override
    @Retry(name = "kafkaSender")
    @TimeLimiter(name = "kafkaSender")
    @Metric(name = "kafka.produce", tags = {"stage=output"})
    public void accept(SinkMessage message) {
        SinkMessageImpl payload = payloadFactory.from(message);
        kafkaTemplate.send(kafkaProperties.getSinkTopic(), payload.labels().toString(), payload);
    }
}
