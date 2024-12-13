
package com.orderservice.service;

import com.orderservice.domain.Order;
import com.orderservice.domain.OrderItem;
import com.orderservice.external.client.SystemAClient;
import com.orderservice.external.client.SystemBClient;
import com.orderservice.external.dto.CalculatedOrderItem;
import com.orderservice.external.dto.CalculatedOrderRequest;
import com.orderservice.external.dto.ProductValidationResponse;
import com.orderservice.exception.OrderProcessingException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalIntegrationService {

    private final SystemAClient systemAClient;
    private final SystemBClient systemBClient;

    @CircuitBreaker(name = "systemA")
    @Retry(name = "systemA")
    public void validateProducts(List<OrderItem> items) {
        log.info("Validating products with System A");

        for (OrderItem item : items) {
            try {
                ProductValidationResponse response = systemAClient.validateProduct(item.getProductCode())
                        .getBody();

                if (response == null || !response.isValid()) {
                    throw new OrderProcessingException("Product validation failed for: " + item.getProductCode());
                }

                if (response.getAvailableQuantity() < item.getQuantity()) {
                    throw new OrderProcessingException(
                            "Insufficient quantity available for product: " + item.getProductCode()
                    );
                }
            } catch (Exception e) {
                log.error("Error validating product: {}", item.getProductCode(), e);
                throw new OrderProcessingException("Failed to validate product with System A", e);
            }
        }
    }

    @CircuitBreaker(name = "systemB")
    @Retry(name = "systemB")
    public void sendCalculatedOrder(Order order) {
        log.info("Sending calculated order to System B: {}", order.getOrderNumber());

        try {
            CalculatedOrderRequest request = buildCalculatedOrderRequest(order);
            systemBClient.sendCalculatedOrder(request);
            log.info("Successfully sent calculated order to System B: {}", order.getOrderNumber());
        } catch (Exception e) {
            log.error("Error sending calculated order to System B: {}", order.getOrderNumber(), e);
            throw new OrderProcessingException("Failed to send calculated order to System B", e);
        }
    }

    private CalculatedOrderRequest buildCalculatedOrderRequest(Order order) {
        List<CalculatedOrderItem> items = order.getItems().stream()
                .map(item -> CalculatedOrderItem.builder()
                        .productCode(item.getProductCode())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .total(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .build())
                .collect(Collectors.toList());

        return CalculatedOrderRequest.builder()
                .orderNumber(order.getOrderNumber())
                .totalAmount(order.getTotalAmount())
                .items(items)
                .build();
    }
}