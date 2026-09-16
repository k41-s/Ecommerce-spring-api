package com.k41s.scrollspree_core.enums;

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
