package com.example.saymontest.exception;

import com.example.saymontest.model.ErrorMessage;
import com.example.saymontest.service.utils.ErrorSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationEvent;


@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final KafkaTemplate<String, ErrorMessage> errorKafkaTemplate;
    private final ErrorSender errorSender;

    @EventListener
    public void handleExceptionEvent(ExceptionEvent event) {
        Throwable ex = event.getException();
        log.error("Global error: {}", ex.getMessage(), ex);
        errorSender.sendError(ex);
    }

    public static class ExceptionEvent extends ApplicationEvent {
        public ExceptionEvent(Throwable ex) {
            super(ex);
        }
        public Throwable getException() {
            return (Throwable) getSource();
        }
    }
}
