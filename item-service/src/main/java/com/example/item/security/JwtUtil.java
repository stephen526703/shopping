package com.example.item.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class JwtUtil {
    private final SecretKey key;
    public JwtUtil(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    public boolean isValid(String token) {
        try { Jwts.parser().verifyWith(key).build().parseSignedClaims(token); return true; }
        catch (Exception e) { return false; }
    }
    public String subject(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }
}
