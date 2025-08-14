package com.example.order.event;

import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface OrderEventRepository extends CassandraRepository<OrderEvent, UUID> {}
