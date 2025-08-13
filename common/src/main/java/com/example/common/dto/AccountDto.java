package com.example.common.dto;

public record AccountDto(Long id, String email, String username,
                         String shippingAddress, String billingAddress, String paymentMethod) {}
