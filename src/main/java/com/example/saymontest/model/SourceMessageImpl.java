package com.example.saymontest.model;

import com.example.saymontest.model.api.SourceMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceMessageImpl implements SourceMessage {

    private long timestamp;
    private Map<String, String> labels;
    private double value;

    @Override
    public long timestamp() {
        return timestamp;
    }

    @Override
    public Map<String, String> labels() {
        return labels == null ? null : Collections.unmodifiableMap(labels);
    }

    @Override
    public double value() {
        return value;
    }
}
