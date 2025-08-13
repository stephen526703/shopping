package com.example.common.events;

import java.util.UUID;

public record OrderCancelledEvent(UUID orderId, String reason) {}
