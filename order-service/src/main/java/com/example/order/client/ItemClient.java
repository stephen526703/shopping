package com.example.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "item-service", url = "${clients.item.url}")
public interface ItemClient {
    record StockUpdateRequest(int delta) {}
    @PatchMapping("/items/{id}/stock")
    void adjustStock(@PathVariable("id") String id, @RequestBody StockUpdateRequest req);
}