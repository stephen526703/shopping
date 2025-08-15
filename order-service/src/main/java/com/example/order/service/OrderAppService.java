package com.example.order.service;

import com.example.common.events.OrderCreatedEvent;
import com.example.common.types.OrderStatus;
import com.example.order.client.ItemClient;
import com.example.order.domain.OrderRow;
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

    public List<OrderRow> all() { return repo.findAll(); }

    public OrderRow get(UUID id) { return repo.findById(id).orElseThrow(); }

    public OrderRow create(OrderCreateRequest r, String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            idempotencyKey = UUID.randomUUID().toString();
        } else {
            var existing = repo.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) return existing.get();
        }

        // Reserve inventory synchronously; throws if not enough stock
        itemClient.adjustStock(r.itemId(), new ItemClient.StockUpdateRequest(-r.quantity()));

        var now = Instant.now();
        var row = OrderRow.builder()
                .orderId(UUID.randomUUID())
                .accountId(r.accountId())
                .itemId(r.itemId())
                .quantity(r.quantity())
                .totalAmountCents(r.totalAmountCents() == null ? 0L : r.totalAmountCents())
                .status(OrderStatus.NEW)
                .idempotencyKey(idempotencyKey)   // now guaranteed non-null
                .createdAt(now)
                .updatedAt(now)
                .build();

        row = repo.save(row);

        var evt = new OrderCreatedEvent(
                row.getOrderId(),
                row.getAccountId(),
                row.getItemId(),
                row.getQuantity(),
                row.getTotalAmountCents(),
                row.getIdempotencyKey()
        );
        orderCreatedTemplate.send(orderCreatedTopic, row.getOrderId().toString(), evt);

        try {
            eventRepo.save(OrderEvent.builder()
                    .orderId(row.getOrderId())
                    .type("ORDER_CREATED")
                    .payloadJson(mapper.writeValueAsString(evt))
                    .createdAt(now)
                    .build());
        } catch (Exception ignored) {}

        return row;
    }

    public OrderRow update(UUID id, OrderUpdateRequest r) {
        var row = repo.findById(id).orElseThrow();
        if (r.quantity() != null && !r.quantity().equals(row.getQuantity())) {
            int delta = r.quantity() - row.getQuantity();
            itemClient.adjustStock(row.getItemId(), new ItemClient.StockUpdateRequest(-delta));
            row.setQuantity(r.quantity());
        }
        if (r.totalAmountCents() != null) row.setTotalAmountCents(r.totalAmountCents());
        if (r.status() != null) row.setStatus(r.status());
        row.setUpdatedAt(Instant.now());
        return repo.save(row);
    }

    public OrderRow cancel(UUID id) {
        var row = repo.findById(id).orElseThrow();
        if (row.getStatus() != OrderStatus.CANCELLED) {
            itemClient.adjustStock(row.getItemId(), new ItemClient.StockUpdateRequest(+row.getQuantity()));
            row.setStatus(OrderStatus.CANCELLED);
            row.setUpdatedAt(Instant.now());
            row = repo.save(row);
        }
        return repo.save(row);
    }

    public void delete(UUID id) { repo.deleteById(id); }
}
