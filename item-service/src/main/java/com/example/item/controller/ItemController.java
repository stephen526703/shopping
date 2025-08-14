package com.example.item.controller;

import com.example.item.domain.Item;
import com.example.item.dto.ItemCreateRequest;
import com.example.item.dto.ItemUpdateRequest;
import com.example.item.dto.StockUpdateRequest;
import com.example.item.service.ItemAppService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemAppService svc;
    public ItemController(ItemAppService svc) { this.svc = svc; }

    @GetMapping public List<Item> all() { return svc.all(); }

    @GetMapping("/{id}") public Item get(@PathVariable("id") String id) { return svc.get(id); }

    @PostMapping public Item create(@Valid @RequestBody ItemCreateRequest req) { return svc.create(req); }

    @PutMapping("/{id}") public Item update(@PathVariable("id") String id, @RequestBody ItemUpdateRequest req) {
        return svc.update(id, req);
    }

    @PatchMapping("/{id}/stock")
    public Item stock(@PathVariable("id") String id, @RequestBody StockUpdateRequest req) {
        return svc.applyStockDelta(id, req.delta());
    }

    @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable String id) {
        svc.delete(id); return ResponseEntity.noContent().build();
    }
}