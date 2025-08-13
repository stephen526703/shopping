package com.example.common.dto;

import com.example.common.types.OrderStatus;
import java.util.UUID;

public final class OrderDtos { private OrderDtos() {}
    public record OrderDto(UUID id, String accountId, String itemId, int quantity,
                           OrderStatus status, Long totalAmountCents) {}
    public record OrderCreateRequest(String accountId, String itemId, int quantity) {}
    public record OrderUpdateRequest(UUID orderId, Integer quantity) {}
}