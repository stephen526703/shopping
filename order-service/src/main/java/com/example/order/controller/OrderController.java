package com.example.order.controller;

import com.example.order.domain.OrderEntity;
import com.example.order.dto.OrderCreateRequest;
import com.example.order.dto.OrderUpdateRequest;
import com.example.order.service.OrderAppService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderAppService svc;
    public OrderController(OrderAppService svc) { this.svc = svc; }

    @GetMapping public List<OrderEntity> all() { return svc.all(); }
    @GetMapping("/{id}") public OrderEntity get(@PathVariable("id") UUID id) { return svc.get(id); }

    @PostMapping public OrderEntity create(@Valid @RequestBody OrderCreateRequest req) { return svc.create(req); }

    @PatchMapping("/{id}") public OrderEntity update(@PathVariable("id") UUID id, @RequestBody OrderUpdateRequest req) {
        return svc.update(id, req);
    }

    @PostMapping("/{id}/cancel") public OrderEntity cancel(@PathVariable("id") UUID id) { return svc.cancel(id); }

    @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        svc.delete(id); return ResponseEntity.noContent().build();
    }
}