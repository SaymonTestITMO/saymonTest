package com.example.saymontest.service.interfaces;

import com.example.saymontest.model.SinkMessageImpl;

import java.util.function.Consumer;

public interface Sink extends Consumer<SinkMessageImpl> {
}
