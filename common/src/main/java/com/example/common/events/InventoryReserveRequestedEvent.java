package com.example.common.events;

import java.util.UUID;

public record InventoryReserveRequestedEvent(UUID orderId, String itemId, int quantity) {}
