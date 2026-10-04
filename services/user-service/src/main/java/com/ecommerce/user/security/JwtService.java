package com.ecommerce.user.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationSeconds;
    public JwtService(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration-seconds:3600}") long expirationSeconds) {
        if (secret.length() < 32) throw new IllegalArgumentException("JWT secret must be at least 32 characters");
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); this.expirationSeconds=expirationSeconds;
    }
    public String generate(UUID userId, String email, String role) {
        Instant now=Instant.now();
        return Jwts.builder().subject(userId.toString()).claim("email",email).claim("role",role)
                .issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(expirationSeconds))).signWith(key).compact();
    }
    public Claims parse(String token) { return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload(); }
    public UUID userId(String token) { return UUID.fromString(parse(token).getSubject()); }
}
