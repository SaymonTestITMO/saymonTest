package com.example.saymontest.pipeline;

import com.example.saymontest.config.PipelineProperties;
import com.example.saymontest.model.SourceMessageImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Grouper {

    private final PipelineProperties pipelineProperties;

    public String groupKey(SourceMessageImpl message) {
        Map<String, String> labels = message.getLabels();
        List<String> groupKeys = pipelineProperties.getGroupByKeys();

        return groupKeys.stream()
                .filter(key -> labels.containsKey(key))
                .sorted()
                .map(key -> key + "=" + labels.get(key))
                .collect(Collectors.joining("|"));
    }
}
