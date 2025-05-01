package com.example.saymontest.service.interfaces;

import com.example.saymontest.model.api.SinkMessage;
import com.example.saymontest.model.SinkMessageImpl;

public interface SinkPayloadFactory {
    SinkMessageImpl from(SinkMessage message);
}
