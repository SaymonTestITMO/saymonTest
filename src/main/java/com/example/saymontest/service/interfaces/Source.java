package com.example.saymontest.service.interfaces;

import com.example.saymontest.model.SourceMessageImpl;

public interface Source {
    Iterable<SourceMessageImpl> source();
}
