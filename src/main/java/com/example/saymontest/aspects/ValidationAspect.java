package com.example.saymontest.aspects;

import com.example.saymontest.exception.ValidationFailedException;
import com.example.saymontest.model.api.SourceMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ValidationAspect {

    private final Validator validator;

    @Before("@annotation(validated) && execution(* com.example.saymontest.service.KafkaSource.*(..)) && args(message)")
    public void validateKafkaMessage(SourceMessage message) {
        log.debug("Validating Kafka message: {}", message);

        Set<ConstraintViolation<SourceMessage>> violations = validator.validate(message);

        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));

            log.error("Invalid message: {}", errorMsg);
            throw new ValidationFailedException("Message validation failed: " + errorMsg);
        }
    }
}


