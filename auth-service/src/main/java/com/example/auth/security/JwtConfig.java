package com.example.auth.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Bean
    public JwtUtil jwtUtil(
            @Value("${jwt.secret:change-me-32+chars-secret-key-1234567890}") String secret,
            @Value("${jwt.ttl-seconds:3600}") long ttlSeconds
    ) {
        return new JwtUtil(secret, ttlSeconds);
    }
}