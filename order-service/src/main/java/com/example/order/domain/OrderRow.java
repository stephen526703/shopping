package com.example.order.domain;

import com.example.common.types.OrderStatus;
import lombok.*;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.Instant;
import java.util.UUID;

@Table("orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderRow {

    @PrimaryKeyColumn(name = "order_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID orderId;

    @Column("account_id")
    private String accountId;

    @Column("item_id")
    private String itemId;

    @Column("quantity")
    private Integer quantity;

    @Column("total_amount_cents")
    private Long totalAmountCents;

    @Column("status")
    private OrderStatus status;

    @Column("idempotency_key")
    private String idempotencyKey;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;
}
