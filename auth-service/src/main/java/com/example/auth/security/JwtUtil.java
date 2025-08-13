package com.example.auth.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class JwtUtil {
    @Value("${app.jwt.secret:dev-secret}") private String secret;
    @Value("${app.jwt.ttlSeconds:3600}") private long ttlSeconds;
    public String generateToken(String username){ var now=new Date(); var exp=new Date(now.getTime()+ttlSeconds*1000); return Jwts.builder().setSubject(username).setIssuedAt(now).setExpiration(exp).signWith(SignatureAlgorithm.HS256, secret).compact(); }
    public String extractUsername(String token){ return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject(); }
}