package com.example.saymontest.model.api;

import jakarta.validation.constraints.*;

import java.util.Map;

public interface SourceMessage {
    @NotNull long timestamp();

    @NotNull
    @Size(min = 1)
    Map<@NotBlank String, @NotBlank String> labels();

    @DecimalMin("0.0")
    double value();
}
