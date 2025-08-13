package com.example.account.dto;

public record AccountDto(
        Long id,
        String email,
        String username,
        String shippingAddress,
        String billingAddress,
        String paymentMethod
) {}