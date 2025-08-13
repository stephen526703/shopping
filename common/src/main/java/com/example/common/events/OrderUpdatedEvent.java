package com.example.common.events;

import java.util.UUID;

public record OrderUpdatedEvent(UUID orderId, int quantity) {}
