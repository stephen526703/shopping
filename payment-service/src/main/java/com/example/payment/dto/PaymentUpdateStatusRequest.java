package com.example.payment.dto;

import jakarta.validation.constraints.NotBlank;

public record PaymentUpdateStatusRequest(@NotBlank String status) {}