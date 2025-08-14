package com.example.auth.security;

import com.example.auth.domain.UserAccount;
import com.example.auth.repo.UserAccountRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserAccountRepository repo;
    public UserService(UserAccountRepository repo){ this.repo = repo; }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount u = repo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
        return new User(
                u.getEmail(),
                u.getPassword() == null ? "" : u.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    public UserAccount loadCurrentEntity(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    public UserDetails loadCurrent(String email) {
        return loadUserByUsername(email);
    }
}
