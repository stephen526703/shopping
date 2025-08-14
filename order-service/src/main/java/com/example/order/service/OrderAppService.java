package com.example.order.service;

import com.example.common.events.OrderCreatedEvent;
import com.example.common.types.OrderStatus;
import com.example.order.client.ItemClient;
import com.example.order.domain.OrderEntity;
import com.example.order.dto.OrderCreateRequest;
import com.example.order.dto.OrderUpdateRequest;
import com.example.order.event.OrderEvent;
import com.example.order.event.OrderEventRepository;
import com.example.order.repo.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderAppService {

    private final OrderRepository repo;
    private final ItemClient itemClient;
    private final KafkaTemplate<String, OrderCreatedEvent> orderCreatedTemplate;
    private final OrderEventRepository eventRepo;
    private final ObjectMapper mapper;

    @Value("${topics.order-created}")
    private String orderCreatedTopic;

    public List<OrderEntity> all() { return repo.findAll(); }

    public OrderEntity get(UUID id) { return repo.findById(id).orElseThrow(); }

    public OrderEntity create(OrderCreateRequest r, String idempotencyKey) {
        // Reserve inventory synchronously; throws if negative stock, etc.
        itemClient.adjustStock(r.itemId(), new ItemClient.StockUpdateRequest(-r.quantity()));

        // Idempotency: reuse order if key provided and exists
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            var existing = repo.findAll().stream()
                    .filter(o -> idempotencyKey.equals(o.getIdempotencyKey()))
                    .findFirst();
            if (existing.isPresent()) return existing.get();
        }

        var o = OrderEntity.builder()
                .accountId(r.accountId())
                .itemId(r.itemId())
                .quantity(r.quantity())
                .totalAmountCents(r.totalAmountCents() == null ? 0L : r.totalAmountCents())
                .status(OrderStatus.NEW)
                .idempotencyKey(idempotencyKey)
                .build();
        o = repo.save(o);

        var evt = new OrderCreatedEvent(
                o.getId(),
                o.getAccountId(),
                o.getItemId(),
                o.getQuantity(),
                o.getTotalAmountCents(),
                idempotencyKey
        );
        orderCreatedTemplate.send(orderCreatedTopic, o.getId().toString(), evt);

        try {
            eventRepo.save(OrderEvent.builder()
                    .id(UUID.randomUUID())
                    .orderId(o.getId())
                    .type("ORDER_CREATED")
                    .payloadJson(mapper.writeValueAsString(evt))
                    .createdAt(Instant.now())
                    .build());
        } catch (Exception ignored) { }

        return o;
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
