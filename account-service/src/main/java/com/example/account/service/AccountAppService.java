package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.dto.*;
import com.example.account.repo.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountAppService {

    private final AccountRepository repo;

    public AccountAppService(AccountRepository repo) { this.repo = repo; }

    public List<AccountDto> all() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    public AccountDto get(Long id) {
        return toDto(repo.findById(id).orElseThrow());
    }

    public AccountDto create(AccountCreateRequest r) {
        if (repo.existsByEmail(r.email())) throw new IllegalArgumentException("email_in_use");
        var acc = Account.builder()
                .email(r.email())
                .username(r.username())
                .shippingAddress(r.shippingAddress())
                .billingAddress(r.billingAddress())
                .paymentMethod(r.paymentMethod())
                .build();
        return toDto(repo.save(acc));
    }

    public AccountDto update(Long id, AccountUpdateRequest r) {
        var a = repo.findById(id).orElseThrow();
        if (r.username() != null) a.setUsername(r.username());
        if (r.shippingAddress() != null) a.setShippingAddress(r.shippingAddress());
        if (r.billingAddress() != null) a.setBillingAddress(r.billingAddress());
        if (r.paymentMethod() != null) a.setPaymentMethod(r.paymentMethod());
        return toDto(repo.save(a));
    }

    public void delete(Long id) { repo.deleteById(id); }

    private AccountDto toDto(Account a) {
        return new AccountDto(
                a.getId(), a.getEmail(), a.getUsername(),
                a.getShippingAddress(), a.getBillingAddress(), a.getPaymentMethod()
        );
    }
}