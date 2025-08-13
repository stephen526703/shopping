package com.example.common.dto;

public record AccountCreateRequest(String email, String username, String password,
                                   String shippingAddress, String billingAddress, String paymentMethod) {}

