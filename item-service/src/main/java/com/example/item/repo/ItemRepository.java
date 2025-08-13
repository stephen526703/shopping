package com.example.item.repo;

import com.example.item.domain.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ItemRepository extends MongoRepository<Item, String> {
    Optional<Item> findByUpc(String upc);
}