package com.example.order.event;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.Instant;
import java.util.UUID;

@Table("order_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEvent {
    @PrimaryKey
    private UUID id;

    @Column("order_id")
    private UUID orderId;

    @Column("type")
    private String type;

    @Column("payload_json")
    private String payloadJson;

    @Column("created_at")
    private Instant createdAt;
}
