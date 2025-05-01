package com.example.saymontest.model;


import com.example.saymontest.model.api.SinkMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SinkMessageImpl implements SinkMessage {

    private long from;
    private long to;
    private Map<String, String> labels;
    private double min;
    private double max;
    private double avg;
    private int count;

    @Override
    public long from() {
        return from;
    }

    @Override
    public long to() {
        return to;
    }

    @Override
    public Map<String, String> labels() {
        return labels == null ? null : Map.copyOf(labels);
    }

    @Override
    public double min() {
        return min;
    }

    @Override
    public double max() {
        return max;
    }

    @Override
    public double avg() {
        return avg;
    }

    @Override
    public int count() {
        return count;
    }
}
