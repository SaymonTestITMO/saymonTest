package com.example.saymontest.pipeline;

import com.example.saymontest.config.PipelineProperties;
import com.example.saymontest.model.SourceMessageImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.expression.Expression;

@Component
@RequiredArgsConstructor
public class Filter {

    private final PipelineProperties pipelineProperties;
    private Expression compiled;

    @PostConstruct
    public void compile() {
        if (pipelineProperties.getFilterRule() != null && !pipelineProperties.getFilterRule().isBlank()) {
            SpelExpressionParser parser = new SpelExpressionParser();
            compiled = parser.parseExpression(pipelineProperties.getFilterRule());
        }
    }

    public boolean passes(SourceMessageImpl message) {
        if (compiled == null)
            return true;
        return Boolean.TRUE.equals(compiled.getValue(message, Boolean.class));
    }
}
