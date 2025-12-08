package com.k41s.ecommerce_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class JwtProperties {
    private String jwtSecret;
    private String jwtIssuer;
    private String jwtAudience;
    private int jwtExpirationHours;
}