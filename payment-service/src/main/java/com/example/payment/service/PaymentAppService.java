package com.example.payment.service;

import com.example.common.types.PaymentStatus;
import com.example.payment.domain.Payment;
import com.example.payment.dto.PaymentCreateRequest;
import com.example.payment.dto.PaymentUpdateStatusRequest;
import com.example.payment.repo.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentAppService {
    private final PaymentRepository repo;

    public PaymentAppService(PaymentRepository repo) { this.repo = repo; }

    public List<Payment> all() { return repo.findAll(); }

    public Payment get(Long id) { return repo.findById(id).orElseThrow(); }

    public Payment create(PaymentCreateRequest r) {
        return repo.findByReference(r.reference()).orElseGet(() -> {
            var p = Payment.builder()
                    .orderId(String.valueOf(r.orderId()))
                    .amountCents(r.amountCents())
                    .currency(r.currency())
                    .reference(r.reference())
                    .status(PaymentStatus.valueOf("PAID"))
                    .build();
            return repo.save(p);
        });
    }

    public Payment updateStatus(Long id, PaymentUpdateStatusRequest req) {
        var p = repo.findById(id).orElseThrow();
        p.setStatus(PaymentStatus.valueOf(req.status()));
        return repo.save(p);
    }

    public Payment refund(Long id, long amountCents) {
        var p = repo.findById(id).orElseThrow();
        if (amountCents >= p.getAmountCents()) p.setStatus(PaymentStatus.valueOf("REFUNDED"));
        else p.setStatus(PaymentStatus.valueOf("PARTIALLY_REFUNDED"));
        return repo.save(p);
    }

    public void delete(Long id) { repo.deleteById(id); }
}