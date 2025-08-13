package com.example.payment.controller;

import com.example.payment.domain.Payment;
import com.example.payment.dto.PaymentCreateRequest;
import com.example.payment.dto.PaymentUpdateStatusRequest;
import com.example.payment.dto.RefundRequest;
import com.example.payment.service.PaymentAppService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentAppService svc;
    public PaymentController(PaymentAppService svc) { this.svc = svc; }

    @GetMapping public List<Payment> all() { return svc.all(); }

    @GetMapping("/{id}") public Payment get(@PathVariable Long id) { return svc.get(id); }

    @PostMapping public Payment create(@Valid @RequestBody PaymentCreateRequest req) { return svc.create(req); }

    @PatchMapping("/{id}/status")
    public Payment updateStatus(@PathVariable Long id, @Valid @RequestBody PaymentUpdateStatusRequest req) {
        return svc.updateStatus(id, req);
    }

    @PostMapping("/{id}/refund")
    public Payment refund(@PathVariable Long id, @Valid @RequestBody RefundRequest req) {
        return svc.refund(id, req.amountCents());
    }

    @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable Long id) {
        svc.delete(id); return ResponseEntity.noContent().build();
    }
}