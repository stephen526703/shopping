package com.example.common.dto;

public record AccountUpdateRequest(String username, String shippingAddress,
                                   String billingAddress, String paymentMethod) {}

