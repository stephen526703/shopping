package com.example.order.dto;

import com.example.common.types.OrderStatus;
import jakarta.validation.constraints.Min;

public record OrderUpdateRequest(
        @Min(1) Integer quantity,
        OrderStatus status,
        Long totalAmountCents
) {}