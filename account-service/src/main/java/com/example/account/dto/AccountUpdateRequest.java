package com.example.account.dto;

public record AccountUpdateRequest(
        String username,
        String shippingAddress,
        String billingAddress,
        String paymentMethod
) {}