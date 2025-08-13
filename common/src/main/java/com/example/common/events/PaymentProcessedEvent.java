package com.example.common.events;

import java.util.UUID;

public record PaymentProcessedEvent(UUID orderId, boolean success, String reference, Long amountCents) {
    public PaymentProcessedEvent(UUID orderId, boolean success, String reference) {
        this(orderId, success, reference, null);
    }
}