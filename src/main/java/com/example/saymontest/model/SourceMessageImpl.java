package com.example.saymontest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceMessageImpl {

    private long timeStamp;
    private Map<String, String> labels;
    private double value;

}
