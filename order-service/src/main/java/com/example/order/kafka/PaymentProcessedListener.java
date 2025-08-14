package com.example.order.kafka;

import com.example.common.events.PaymentProcessedEvent;
import com.example.common.types.OrderStatus;
import com.example.order.domain.OrderRow;
import com.example.order.event.OrderEvent;
import com.example.order.event.OrderEventRepository;
import com.example.order.repo.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProcessedListener {

    private final OrderRepository orders;
    private final OrderEventRepository events;
    private final ObjectMapper mapper;

    @Value("${topics.payment-processed}")
    private String paymentProcessedTopic;

    @KafkaListener(
            topics = "${topics.payment-processed:payment-processed}",
            groupId = "${spring.kafka.consumer.group-id:order-service}"
    )
    public void handle(PaymentProcessedEvent evt) {
        UUID orderId = evt.orderId();
        boolean success = evt.success();

        Optional<OrderRow> maybe = orders.findById(orderId);
        if (maybe.isEmpty()) {
            log.warn("PaymentProcessed for unknown orderId={} (success={}), ignoring.", orderId, success);
            persistEventSafely(orderId, success, evt);
            return;
        }

        OrderRow row = maybe.get();
        OrderStatus current = row.getStatus();
        OrderStatus target = success ? OrderStatus.PAID : OrderStatus.PAYMENT_FAILED;

        if (current == target) {
            log.info("Duplicate or redundant payment event for orderId={}, already at status={}. No-op.", orderId, current);
            persistEventSafely(orderId, success, evt);
            return;
        }

        row.setStatus(target);
        row.setUpdatedAt(Instant.now());
        orders.save(row);
        persistEventSafely(orderId, success, evt);
        log.info("Applied payment result for orderId={} -> status={}", orderId, target);
    }

    private void persistEventSafely(UUID orderId, boolean success, PaymentProcessedEvent evt) {
        try {
            String json = mapper.writeValueAsString(evt);
            events.save(OrderEvent.builder()
                    .orderId(orderId)
                    .type("PAYMENT_PROCESSED:" + (success ? "SUCCESS" : "FAILED"))
                    .payloadJson(json)
                    .createdAt(Instant.now())
                    .build());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize PaymentProcessedEvent for orderId={}: {}", orderId, e.getMessage(), e);
        }
    }
}
