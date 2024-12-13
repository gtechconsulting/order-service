package com.orderservice.external.client;

import com.orderservice.external.dto.CalculatedOrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "system-b", url = "${external.system.b.url}")
public interface SystemBClient {
    @PostMapping("/orders/calculated")
    ResponseEntity<Void> sendCalculatedOrder(@RequestBody CalculatedOrderRequest request);
}