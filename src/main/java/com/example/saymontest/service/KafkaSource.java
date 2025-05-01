package com.example.saymontest.service;

import com.example.saymontest.aspects.annotations.Metric;
import com.example.saymontest.aspects.annotations.Validated;
import com.example.saymontest.model.SourceMessageImpl;
import com.example.saymontest.model.api.SourceMessage;
import com.example.saymontest.pipeline.ProcessorPipeline;
import com.example.saymontest.service.interfaces.Source;
import com.example.saymontest.service.interfaces.SourceMessageFactory;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaSource implements Source {

    private final ProcessorPipeline processorPipeline;
    private final SourceMessageFactory factory;

    @KafkaListener(topics = "${kafka.source-topic}", groupId = "pipeline-group", errorHandler = "kafkaValidationErrorHandler")
    @Validated
    @Metric(name = "kafka.consume", tags = {"stage=ingestion"})
    public void listen(ConsumerRecord<String, SourceMessageImpl> record, Acknowledgment ack) {
        processorPipeline.process(factory.from(record.value()));
        ack.acknowledge();
    }

    @Override
    public Iterable<SourceMessage> source() {
        return null;
    }
}
