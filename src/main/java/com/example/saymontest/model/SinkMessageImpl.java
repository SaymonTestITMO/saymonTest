package com.example.saymontest.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SinkMessageImpl {

    private long from;
    private long to;
    private Map<String, String> labels;
    private double min;
    private double max;
    private double avg;
    private int count;

}
