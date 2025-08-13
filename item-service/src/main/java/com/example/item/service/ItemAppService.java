package com.example.item.service;

import com.example.item.domain.Item;
import com.example.item.dto.ItemCreateRequest;
import com.example.item.dto.ItemUpdateRequest;
import com.example.item.repo.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemAppService {
    private final ItemRepository repo;

    public ItemAppService(ItemRepository repo) { this.repo = repo; }

    public List<Item> all() { return repo.findAll(); }

    public Item get(String id) { return repo.findById(id).orElseThrow(); }

    public Item create(ItemCreateRequest r) {
        var item = new Item();
        item.setUpc(r.upc());
        item.setName(r.name());
        item.setPrice(r.price());
        item.setImageUrl(r.imageUrl());
        item.setStock(r.stock());
        item.setAttributes(r.attributes());
        return repo.save(item);
    }

    public Item update(String id, ItemUpdateRequest r) {
        var i = repo.findById(id).orElseThrow();
        if (r.name() != null) i.setName(r.name());
        if (r.price() != null) i.setPrice(r.price());
        if (r.imageUrl() != null) i.setImageUrl(r.imageUrl());
        if (r.stock() != null) i.setStock(r.stock());
        if (r.attributes() != null) i.setAttributes(r.attributes());
        return repo.save(i);
    }

    @Transactional
    public Item applyStockDelta(String id, int delta) {
        var i = repo.findById(id).orElseThrow();
        int newStock = Math.max(0, i.getStock() + delta);
        i.setStock(newStock);
        return repo.save(i);
    }

    public void delete(String id) { repo.deleteById(id); }
}