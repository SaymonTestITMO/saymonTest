package com.example.saymontest.service.interfaces;

import com.example.saymontest.model.api.SinkMessage;

import java.util.function.Consumer;

public interface Sink extends Consumer<SinkMessage> {
}
