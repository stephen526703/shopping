package com.example.order.repo;

import com.example.order.domain.OrderRow;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends CassandraRepository<OrderRow, UUID> {

    @Query("SELECT * FROM orders WHERE idempotency_key = ?0 LIMIT 1")
    Optional<OrderRow> findByIdempotencyKey(String idempotencyKey);
}
