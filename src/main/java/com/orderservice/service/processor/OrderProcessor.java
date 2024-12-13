package com.orderservice.service.processor;

import com.orderservice.domain.Order;
import com.orderservice.domain.OrderStatus;
import com.orderservice.exception.OrderProcessingException;
import com.orderservice.external.client.SystemBClient;
import com.orderservice.external.dto.CalculatedOrderItem;
import com.orderservice.external.dto.CalculatedOrderRequest;
import com.orderservice.service.calculation.OrderCalculationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProcessor {

    private final OrderCalculationStrategy calculationStrategy;
    private final SystemBClient systemBClient;

    public Order process(Order order) {
        log.info("Processing order: {}", order.getOrderNumber());

        try {
            // Calcula o valor total
            BigDecimal totalAmount = calculationStrategy.calculate(order);
            order.setTotalAmount(totalAmount);
            order.setStatus(OrderStatus.CALCULATED);

            // Envia para o Sistema B
            systemBClient.sendCalculatedOrder(mapToCalculatedRequest(order));

            order.setStatus(OrderStatus.COMPLETED);
            log.info("Order processed successfully: {}", order.getOrderNumber());

            return order;

        } catch (Exception e) {
            log.error("Error processing order: {}", order.getOrderNumber(), e);
            order.setStatus(OrderStatus.ERROR);
            throw new OrderProcessingException("Failed to process order", e);
        }
    }

    private CalculatedOrderRequest mapToCalculatedRequest(Order order) {
        return CalculatedOrderRequest.builder()
                .orderNumber(order.getOrderNumber())
                .totalAmount(order.getTotalAmount())
                .items(order.getItems().stream()
                        .map(item -> CalculatedOrderItem.builder()
                                .productCode(item.getProductCode())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .total(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}