package com.example.order.service;

import com.example.order.domain.OrderEntity;
import com.example.order.domain.OrderStatus;
import com.example.order.dto.OrderCreateRequest;
import com.example.order.dto.OrderUpdateRequest;
import com.example.order.repo.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderAppService {
    private final OrderRepository repo;

    public OrderAppService(OrderRepository repo) { this.repo = repo; }

    public List<OrderEntity> all() { return repo.findAll(); }

    public OrderEntity get(UUID id) { return repo.findById(id).orElseThrow(); }

    public OrderEntity create(OrderCreateRequest r) {
        var o = new OrderEntity();
        o.setAccountId(r.accountId());
        o.setItemId(r.itemId());
        o.setQuantity(r.quantity());
        if (r.totalAmountCents() != null) o.setTotalAmountCents(r.totalAmountCents());
        o.setStatus(OrderStatus.NEW);
        return repo.save(o);
    }

    public OrderEntity update(UUID id, OrderUpdateRequest r) {
        var o = repo.findById(id).orElseThrow();
        if (r.quantity() != null) o.setQuantity(r.quantity());
        if (r.totalAmountCents() != null) o.setTotalAmountCents(r.totalAmountCents());
        if (r.status() != null) o.setStatus(r.status());
        return repo.save(o);
    }

    public OrderEntity cancel(UUID id) {
        var o = repo.findById(id).orElseThrow();
        o.setStatus(OrderStatus.CANCELLED);
        return repo.save(o);
    }

    public void delete(UUID id) { repo.deleteById(id); }
}