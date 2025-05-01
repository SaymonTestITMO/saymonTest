package com.example.saymontest.service.utils;

import com.example.saymontest.model.SourceMessageImpl;
import com.example.saymontest.model.api.SourceMessage;
import com.example.saymontest.service.interfaces.SourceMessageFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultSourceMessageFactory implements SourceMessageFactory {

    @Override
    public SourceMessage from(SourceMessageImpl source) {
        return source;
    }
}
