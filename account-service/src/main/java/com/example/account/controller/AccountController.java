package com.example.account.controller;

import com.example.account.dto.*;
import com.example.account.service.AccountAppService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountAppService svc;

    public AccountController(AccountAppService svc) { this.svc = svc; }

    @GetMapping public List<AccountDto> all() { return svc.all(); }

    @GetMapping("/{id}") public AccountDto get(@PathVariable("id") Long id) { return svc.get(id); }

    @PostMapping public AccountDto create(@Valid @RequestBody AccountCreateRequest req) { return svc.create(req); }

    @PutMapping("/{id}") public AccountDto update(@PathVariable("id") Long id, @RequestBody AccountUpdateRequest req) {
        return svc.update(id, req);
    }

    @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        svc.delete(id); return ResponseEntity.noContent().build();
    }
}