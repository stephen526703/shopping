package com.example.common.events;

import java.util.UUID;

public record PaymentReversedEvent(UUID orderId, String reference, Long amountCents, String reason) {}
