package com.example.common.dto;
import java.util.Map;
public record ItemCreateRequest(String name, String upc, double price,
                                String imageUrl, int stock, Map<String, Object> attributes) {}

