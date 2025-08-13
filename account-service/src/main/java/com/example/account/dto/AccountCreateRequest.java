package com.example.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AccountCreateRequest(
        @NotBlank @Email String email,
        @NotBlank String username,
        String shippingAddress,
        String billingAddress,
        String paymentMethod
) {}