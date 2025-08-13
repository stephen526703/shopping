package com.example.item.dto;

import jakarta.validation.constraints.Min;
import java.util.Map;

public record ItemUpdateRequest(
        String name,
        @Min(0) Double price,
        String imageUrl,
        @Min(0) Integer stock,
        Map<String, Object> attributes
) {}
