package com.orderservice.external.client;

import com.orderservice.external.dto.ProductValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "system-a", url = "${external.system.a.url}")
public interface SystemAClient {
    @GetMapping("/products/{code}/validate")
    ResponseEntity<ProductValidationResponse> validateProduct(@PathVariable("code") String productCode);
}