package com.example.auth.service;
import com.example.auth.domain.UserAccount;
import com.example.auth.repo.UserAccountRepository;
import com.example.auth.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthAppService {
    private final UserAccountRepository repo; private final PasswordEncoder encoder; private final JwtUtil jwt;
    public AuthAppService(UserAccountRepository repo, PasswordEncoder encoder, JwtUtil jwt){ this.repo=repo; this.encoder=encoder; this.jwt=jwt; }
    public void register(String email, String password){ if(repo.existsByEmail(email)) throw new IllegalArgumentException("email_in_use"); var u=new UserAccount(); u.setEmail(email); u.setPassword(encoder.encode(password)); repo.save(u); }
    public String login(String email, String password){ var u=repo.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("bad_credentials")); if(u.getPassword()==null || !encoder.matches(password, u.getPassword())) throw new IllegalArgumentException("bad_credentials"); return jwt.generateToken(u.getEmail()); }
}