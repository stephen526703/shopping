package com.example.auth.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.auth.service.AuthAppService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController @RequestMapping("/auth")
public class AuthController {
    private record RegisterRequest(@JsonProperty("email") String email, @JsonProperty("password") String password){}
    private record LoginRequest(@JsonProperty("email") String email, @JsonProperty("password") String password){}
    private record LoginResponse(@JsonProperty("token") String token){}
    private final AuthAppService app;
    public AuthController(AuthAppService app){ this.app=app; }
    @PostMapping("/register") public ResponseEntity<?> register(@RequestBody RegisterRequest r){ app.register(r.email(), r.password()); return ResponseEntity.ok().build(); }
    @PostMapping("/login") public LoginResponse login(@RequestBody LoginRequest req){ return new LoginResponse(app.login(req.email(), req.password())); }
}