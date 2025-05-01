package com.example.saymontest.exception;

public class DeduplicationException extends RuntimeException {
    public DeduplicationException(String message) {
        super(message);
    }
}
