package com.example.payment.service;

import com.example.common.events.PaymentProcessedEvent;
import com.example.common.events.PaymentReversedEvent;
import com.example.common.types.PaymentStatus;
import com.example.payment.domain.Payment;
import com.example.payment.dto.PaymentCreateRequest;
import com.example.payment.dto.PaymentUpdateStatusRequest;
import com.example.payment.repo.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentAppService {

    private final PaymentRepository repo;

    private final KafkaTemplate<String, PaymentProcessedEvent> paymentProcessedTemplate;
    private final KafkaTemplate<String, PaymentReversedEvent> paymentReversedTemplate;

    @Value("${topics.payment-processed:payments.payment.processed}")
    private String paymentProcessedTopic;

    @Value("${topics.payment-reversed:payments.payment.reversed}")
    private String paymentReversedTopic;

    public List<Payment> all() { return repo.findAll(); }

    public Payment get(Long id) { return repo.findById(id).orElseThrow(); }

    public Payment create(PaymentCreateRequest r) {
        Payment saved = repo.findByReference(r.reference()).orElseGet(() -> {
            var p = Payment.builder()
                    .orderId(r.orderId().toString())
                    .amountCents(r.amountCents())
                    .currency(r.currency())
                    .reference(r.reference())
                    .status(PaymentStatus.PAID)
                    .build();
            return repo.save(p);
        });

        boolean success = saved.getStatus() == PaymentStatus.PAID;
        paymentProcessedTemplate.send(
                paymentProcessedTopic,
                saved.getOrderId(),
                new PaymentProcessedEvent(
                        UUID.fromString(saved.getOrderId()),
                        success,
                        saved.getReference(),
                        saved.getAmountCents()
                )
        );
        return saved;
    }

    public Payment updateStatus(Long id, PaymentUpdateStatusRequest req) {
        var p = repo.findById(id).orElseThrow();

        PaymentStatus newStatus = parseStatus(req.status());
        p.setStatus(newStatus);
        var saved = repo.save(p);

        boolean success = saved.getStatus() == PaymentStatus.PAID;
        paymentProcessedTemplate.send(
                paymentProcessedTopic,
                saved.getOrderId(),
                new PaymentProcessedEvent(
                        UUID.fromString(saved.getOrderId()),
                        success,
                        saved.getReference(),
                        saved.getAmountCents()
                )
        );
        return saved;
    }

    public Payment refund(Long id, long amountCents) {
        var p = repo.findById(id).orElseThrow();

        if (amountCents >= p.getAmountCents()) {
            p.setStatus(PaymentStatus.REFUNDED);
        }

        var saved = repo.save(p);

        paymentReversedTemplate.send(
                paymentReversedTopic,
                saved.getOrderId(),
                new PaymentReversedEvent(
                        UUID.fromString(saved.getOrderId()),
                        saved.getReference(),
                        amountCents,
                        (amountCents >= saved.getAmountCents()) ? "FULL_REFUND" : "PARTIAL_REFUND"
                )
        );
        return saved;
    }

    public void delete(Long id) { repo.deleteById(id); }


    private static PaymentStatus parseStatus(String raw) {
        if (raw == null) throw new IllegalArgumentException("status is required");
        try {
            return PaymentStatus.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unsupported status: " + raw + " (allowed: PENDING, PAID, REFUNDED, FAILED)");
        }
    }
}