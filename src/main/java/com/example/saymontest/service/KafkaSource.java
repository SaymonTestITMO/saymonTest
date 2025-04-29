package com.example.saymontest.service;

import com.example.saymontest.model.SourceMessageImpl;
import com.example.saymontest.service.interfaces.Source;

public class KafkaSource implements Source {

    @Override
    public Iterable<SourceMessageImpl> source() {
        return null;
    }
}
