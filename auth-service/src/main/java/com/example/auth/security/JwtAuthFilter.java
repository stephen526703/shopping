package com.example.auth.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class JwtAuthFilter extends GenericFilter {
    private final JwtUtil jwt; private final UserService userService;
    public JwtAuthFilter(JwtUtil jwt, UserService userService){ this.jwt=jwt; this.userService=userService; }
    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var req=(HttpServletRequest)request; var auth=req.getHeader("Authorization");
        if(auth!=null && auth.startsWith("Bearer ")){
            var token=auth.substring(7);
            try {
                var username=jwt.extractUsername(token);
                if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                    UserDetails user=userService.loadUserByUsername(username);
                    var authToken=new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception ignored) { }
        }
        chain.doFilter(request,response);
    }
}