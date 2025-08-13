package com.example.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RefundRequest(@NotNull @Min(0) Long amountCents) {}