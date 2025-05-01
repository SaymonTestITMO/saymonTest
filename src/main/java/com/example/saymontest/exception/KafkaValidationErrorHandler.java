package com.example.saymontest.exception;

import com.example.saymontest.service.utils.ErrorSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component("kafkaValidationErrorHandler")
@RequiredArgsConstructor
public class KafkaValidationErrorHandler implements ConsumerAwareListenerErrorHandler {

    private final ErrorSender errorSender;

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
        Throwable rootCause = getRootCause(exception);

        if (rootCause instanceof ValidationFailedException || rootCause instanceof AggregationException ||
                rootCause instanceof DeduplicationException) {
            log.warn("Kafka validation failed: {}\nMessage: {}", rootCause.getMessage(), message.getPayload());
            errorSender.sendError(rootCause);
            return null;
        }
        throw exception;
    }

    private Throwable getRootCause(Throwable ex) {
        Throwable cause = ex;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }
}
