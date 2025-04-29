package com.example.saymontest.service;

import com.example.saymontest.model.SourceMessageImpl;
import com.example.saymontest.pipeline.ProcessorPipeline;
import com.example.saymontest.service.interfaces.Source;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaSource implements Source {

    private final ProcessorPipeline processorPipeline;

    @KafkaListener(topics = "${kafka.source-topic}", groupId = "pipeline-group")
    public void listen(ConsumerRecord<String, SourceMessageImpl> record, Acknowledgment ack) {
        processorPipeline.process(record.value());
        ack.acknowledge();
    }

    @Override
    public Iterable<SourceMessageImpl> source() {
        return null;
    }

}
