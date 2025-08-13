package com.example.common.dto;
import java.util.Map;
public record ItemDto(String id, String name, String upc, double price,
                      String imageUrl, int stock, Map<String, Object> attributes) {}

