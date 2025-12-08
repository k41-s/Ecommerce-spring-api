package com.k41s.ecommerce_api.exceptions;

public class JwtMalformedException extends RuntimeException {
    public JwtMalformedException(String message) {
        super(message);
    }
}
