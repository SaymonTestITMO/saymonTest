package com.example.saymontest.service.interfaces;

import com.example.saymontest.model.SourceMessageImpl;
import org.springframework.stereotype.Component;

public interface Source {
    Iterable<SourceMessageImpl> source();
}
