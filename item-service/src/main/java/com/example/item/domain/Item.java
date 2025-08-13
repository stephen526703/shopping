package com.example.item.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Map;
import lombok.*;

@Document("items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    @Id private String id;
    @Indexed(unique = true) private String upc;
    private String name;
    private double price;
    private String imageUrl;
    private int stock;
    private Map<String, Object> attributes;

    }