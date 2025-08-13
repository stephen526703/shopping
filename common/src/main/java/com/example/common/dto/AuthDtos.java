package com.example.common.dto;

public final class AuthDtos { private AuthDtos() {}
    public record LoginRequest(String email, String password) {}
    public record LoginResponse(String token) {}
    public record RegisterRequest(String email, String password, String username) {}
}