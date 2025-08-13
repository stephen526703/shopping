package com.example.common.events;
import java.util.UUID;

public record OrderCreatedEvent(UUID orderId, String accountId, String itemId, int quantity,
                                Long amountCents, String idempotencyKey) {
    public OrderCreatedEvent(UUID orderId, String accountId, String itemId, int quantity) {
        this(orderId, accountId, itemId, quantity, null, null);
    }
}
