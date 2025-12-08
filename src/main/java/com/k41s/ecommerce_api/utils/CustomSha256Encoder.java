package com.k41s.ecommerce_api.utils;

import com.google.common.hash.Hashing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;

public class CustomSha256Encoder implements PasswordEncoder {

    private String generateHash(CharSequence rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        return Hashing.sha256()
                .hashString(rawPassword, StandardCharsets.UTF_8)
                .toString();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return generateHash(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        String calculatedHash = generateHash(rawPassword);
        return calculatedHash.equals(encodedPassword);
    }
}
