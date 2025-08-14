package com.example.account.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwt;
    public JwtAuthFilter(JwtUtil jwt) { this.jwt = jwt; }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws java.io.IOException, jakarta.servlet.ServletException {
        String auth = req.getHeader("Authorization");
        if (StringUtils.hasText(auth) && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            if (jwt.isValid(token)) {
                var authn = new UsernamePasswordAuthenticationToken(jwt.subject(token), null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER")));
                authn.setDetails(req);
                org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authn);
            }
        }
        chain.doFilter(req, res);
    }
}
