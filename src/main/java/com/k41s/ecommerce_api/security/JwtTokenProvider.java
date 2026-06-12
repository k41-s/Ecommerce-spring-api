package com.k41s.ecommerce_api.security;

import com.k41s.ecommerce_api.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final SecretKey key;
    private final String issuer;
    private final String audience;
    private final long accessExpirationMillis;
    private final long refreshExpirationMillis;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.issuer = jwtProperties.getJwtIssuer();
        this.audience = jwtProperties.getJwtAudience();
        this.accessExpirationMillis = TimeUnit.MINUTES.toMillis(jwtProperties.getJwtExpirationMinutes());
        this.refreshExpirationMillis = TimeUnit.DAYS.toMillis(jwtProperties.getJwtRefreshExpirationDays());

        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getJwtSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);    }

    public String generateAccessToken(Authentication authentication) {
        return buildToken(authentication, this.accessExpirationMillis, "access");
    }

    public String generateRefreshToken(Authentication authentication) {
        return buildToken(authentication, this.refreshExpirationMillis, "refresh");
    }

    private String buildToken(Authentication authentication, long expirationMillis, String tokenType) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("roles", roles)
                .claim("token_type", tokenType)
                .setIssuer(this.issuer)
                .setAudience(this.audience)
                .signWith(this.key)
                .compact();
    }

    public String getRolesFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("roles", String.class);
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String getTokenTypeFromJWT(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(token).getBody();
        return claims.get("token_type", String.class);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(this.key)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty");
        }
        return false;
    }
}