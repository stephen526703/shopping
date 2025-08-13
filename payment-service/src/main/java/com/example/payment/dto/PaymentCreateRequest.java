package com.example.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PaymentCreateRequest(
        @NotNull UUID orderId,
        @NotNull @Min(0) Long amountCents,
        @NotBlank String currency,
        @NotBlank String reference
) {}