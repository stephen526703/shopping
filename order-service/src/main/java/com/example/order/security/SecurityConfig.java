package com.example.order.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean JwtUtil jwtUtil(@Value("${jwt.secret:change-me-32+chars-secret-key-1234567890}") String secret) { return new JwtUtil(secret); }

    @Bean
    public SecurityFilterChain filter(HttpSecurity http, JwtUtil jwt) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers("/v3/api-docs/**","/swagger-ui/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthFilter(jwt), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
