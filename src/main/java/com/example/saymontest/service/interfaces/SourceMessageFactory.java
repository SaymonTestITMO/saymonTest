package com.example.saymontest.service.interfaces;

import com.example.saymontest.model.SourceMessageImpl;
import com.example.saymontest.model.api.SourceMessage;

public interface SourceMessageFactory {
    SourceMessage from(SourceMessageImpl source);
}