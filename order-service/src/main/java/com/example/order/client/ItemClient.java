package com.example.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "item-service", url = "${clients.item.url}")
public interface ItemClient {
    record StockUpdateRequest(int delta) {}
    @PostMapping(value = "/items/{id}/stock/adjust", consumes = "application/json")
    Item adjustStock(@PathVariable("id") String id,
                     @RequestBody StockUpdateRequest req);

    record Item(String id, String name, Integer stock) {}
}