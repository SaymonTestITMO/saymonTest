package com.example.saymontest.pipeline;

import com.example.saymontest.aspects.annotations.Metric;
import com.example.saymontest.config.PipelineProperties;
import com.example.saymontest.model.api.SourceMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.expression.Expression;

@Component
@Slf4j
@RequiredArgsConstructor
public class Filter {

    private final PipelineProperties pipelineProperties;
    private Expression compiled;

    @PostConstruct
    public void compile() {
        if (pipelineProperties.getFilterRule() != null && !pipelineProperties.getFilterRule().isBlank()) {
            try {
                SpelExpressionParser parser = new SpelExpressionParser();
                compiled = parser.parseExpression(pipelineProperties.getFilterRule());
            } catch (Exception e) {
                throw new IllegalStateException("Parsing filter rules failed: " + e.getMessage());
            }
        }
    }

    @Metric(name = "filter", tags = {"stage=preprocessing"})
    public boolean passes(SourceMessage message) {
        if (compiled == null) {
            return true;
        }
        try {
            Boolean result = compiled.getValue(message, Boolean.class);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.warn("Filter evaluation error for message {}: {}", message, e.getMessage());
            return false;
        }
    }
}
