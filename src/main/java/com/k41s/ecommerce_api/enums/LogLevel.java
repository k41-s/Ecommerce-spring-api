package com.k41s.ecommerce_api.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum LogLevel {
    @JsonProperty("Information")
    INFORMATION,
    @JsonProperty("Warning")
    WARNING,
    ERROR,
    @JsonProperty("Debug")
    DEBUG
}
