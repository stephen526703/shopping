package com.example.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderCreateRequest(
        @NotBlank String accountId,
        @NotBlank String itemId,
        @NotNull @Min(1) Integer quantity,
        Long totalAmountCents
) {}