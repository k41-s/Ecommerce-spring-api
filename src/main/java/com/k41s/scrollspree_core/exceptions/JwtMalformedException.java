package com.k41s.scrollspree_core.exceptions;

public class JwtMalformedException extends RuntimeException {
    public JwtMalformedException(String message) {
        super(message);
    }
}
