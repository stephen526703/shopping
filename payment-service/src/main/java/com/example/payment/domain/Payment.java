package com.example.payment.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "payments",
        uniqueConstraints = @UniqueConstraint(name = "uk_payment_reference", columnNames = "reference")
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Store UUID as BINARY(16) in MySQL; Hibernate 6 handles UUID↔binary
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private Long amountCents;

    @Column(nullable = false, length = 64)
    private String currency; // e.g., "USD"

    @Column(nullable = false, length = 128)
    private String reference; // idempotency/business ref

    @Column(nullable = false, length = 32)
    private String status; // PENDING, PAID, REFUNDED, FAILED

    @Column(nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @PreUpdate
    void onUpdate() { this.updatedAt = OffsetDateTime.now(); }
}