package com.example.auth.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtAuthFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws java.io.IOException, jakarta.servlet.ServletException {
        String auth = req.getHeader("Authorization");
        if (StringUtils.hasText(auth) && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            if (jwtUtil.isValid(token)) {
                var user = userService.loadCurrent(jwtUtil.getSubject(token)); // implement loadCurrent if missing
                var authn = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authn);
            }
        }
        chain.doFilter(req, res);
    }
}
