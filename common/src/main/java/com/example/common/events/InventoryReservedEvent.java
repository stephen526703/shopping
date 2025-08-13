package com.example.common.events;

import java.util.UUID;

public record InventoryReservedEvent(UUID orderId, String itemId, int quantity, boolean success, String reason) {}
