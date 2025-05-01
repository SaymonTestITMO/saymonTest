package com.example.saymontest.service.utils;

import com.example.saymontest.model.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ErrorSender {

    private final KafkaTemplate<String, ErrorMessage> errorKafkaTemplate;

    public void sendError(Throwable ex) {
        ErrorMessage error = new ErrorMessage(
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                Instant.now()
        );
        errorKafkaTemplate.send("errors", error);
    }
}