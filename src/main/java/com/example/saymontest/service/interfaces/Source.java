package com.example.saymontest.service.interfaces;

import com.example.saymontest.model.api.SourceMessage;

public interface Source {
    Iterable<SourceMessage> source();
}
