package com.example.payment.service;

import com.example.payment.domain.Payment;
import com.example.payment.dto.PaymentCreateRequest;
import com.example.payment.dto.PaymentUpdateStatusRequest;
import com.example.payment.repo.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentAppService {
    private final PaymentRepository repo;

    public PaymentAppService(PaymentRepository repo) { this.repo = repo; }

    public List<Payment> all() { return repo.findAll(); }

    public Payment get(Long id) { return repo.findById(id).orElseThrow(); }

    public Payment create(PaymentCreateRequest r) {
        // Idempotent by reference
        return repo.findByReference(r.reference()).orElseGet(() -> {
            var p = Payment.builder()
                    .orderId(r.orderId())
                    .amountCents(r.amountCents())
                    .currency(r.currency())
                    .reference(r.reference())
                    .status("PAID") // for now; later this can be PENDING then captured
                    .build();
            return repo.save(p);
        });
    }

    public Payment updateStatus(Long id, PaymentUpdateStatusRequest req) {
        var p = repo.findById(id).orElseThrow();
        p.setStatus(req.status());
        return repo.save(p);
    }

    public Payment refund(Long id, long amountCents) {
        var p = repo.findById(id).orElseThrow();
        // naive: mark REFUNDED if refund covers full amount (adapt later as needed)
        if (amountCents >= p.getAmountCents()) p.setStatus("REFUNDED");
        else p.setStatus("PARTIALLY_REFUNDED");
        return repo.save(p);
    }

    public void delete(Long id) { repo.deleteById(id); }
}