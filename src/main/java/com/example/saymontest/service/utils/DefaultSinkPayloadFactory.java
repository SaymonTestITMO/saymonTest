package com.example.saymontest.service.utils;

import com.example.saymontest.model.SinkMessageImpl;
import com.example.saymontest.model.api.SinkMessage;
import com.example.saymontest.service.interfaces.SinkPayloadFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultSinkPayloadFactory implements SinkPayloadFactory {
    @Override
    public SinkMessageImpl from(SinkMessage msg) {
        if (msg instanceof SinkMessageImpl impl) {
            return impl;
        }
        return new SinkMessageImpl(
                msg.from(),
                msg.to(),
                msg.labels(),
                msg.min(),
                msg.max(),
                msg.avg(),
                msg.count()
        );
    }
}

