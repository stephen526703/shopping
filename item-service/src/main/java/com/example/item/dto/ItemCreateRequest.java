package com.example.item.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record ItemCreateRequest(
        @NotBlank String upc,
        @NotBlank String name,
        @NotNull @Min(0) Double price,
        String imageUrl,
        @NotNull @Min(0) Integer stock,
        Map<String, Object> attributes
) {}