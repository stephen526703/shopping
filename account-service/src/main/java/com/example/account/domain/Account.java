package com.example.account.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "accounts",
        uniqueConstraints = @UniqueConstraint(name = "uk_account_email", columnNames = "email")
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 320, columnDefinition = "VARCHAR(320) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin")
    private String email;

    @Column(nullable = false, length = 64)
    private String username;

    @Column(length = 512)
    private String shippingAddress;

    @Column(length = 512)
    private String billingAddress;

    @Column(length = 128)
    private String paymentMethod;
}