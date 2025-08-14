package com.example.order.kafka;

import com.example.common.events.PaymentProcessedEvent;
import com.example.common.types.OrderStatus;
import com.example.order.event.OrderEvent;
import com.example.order.event.OrderEventRepository;
import com.example.order.repo.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentProcessedListener {

    private final OrderRepository orders;
    private final OrderEventRepository events;
    private final ObjectMapper mapper;

    @Value("${topics.payment-processed}")
    private String paymentProcessedTopic;

    @KafkaListener(
            topics = "#{__listener.paymentProcessedTopic}",
            groupId = "order-service",
            containerFactory = "paymentProcessedKafkaFactory"
    )
    public void handle(PaymentProcessedEvent evt) throws Exception {
        orders.findById(evt.orderId()).ifPresent(order -> {
            order.setStatus(evt.success() ? OrderStatus.PAID : OrderStatus.PAYMENT_FAILED);
            orders.save(order);
        });

        var json = mapper.writeValueAsString(evt);
        events.save(OrderEvent.builder()
                .id(UUID.randomUUID())
                .orderId(evt.orderId())
                .type("PAYMENT_PROCESSED:" + (evt.success() ? "SUCCESS" : "FAILED"))
                .payloadJson(json)
                .createdAt(Instant.now())
                .build());
    }
}
