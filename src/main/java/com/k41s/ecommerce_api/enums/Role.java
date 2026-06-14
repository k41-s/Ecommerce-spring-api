package com.k41s.ecommerce_api.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("Admin")
    ADMIN,
    @JsonProperty("User")
    USER
}
