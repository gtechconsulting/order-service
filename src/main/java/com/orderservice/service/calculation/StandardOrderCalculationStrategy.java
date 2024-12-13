package com.orderservice.service.calculation;

import com.orderservice.domain.Order;
import com.orderservice.domain.OrderItem;
import com.orderservice.external.client.SystemAClient;
import com.orderservice.external.dto.ProductValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Slf4j
public class StandardOrderCalculationStrategy implements OrderCalculationStrategy {

    private final SystemAClient systemAClient;

    @Override
    @Cacheable(value = "orderCalculations", key = "#order.id")
    public BigDecimal calculate(Order order) {
        log.info("Calculating total for order: {}", order.getOrderNumber());

        return order.getItems().stream()
                .map(this::calculateItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateItemTotal(OrderItem item) {
        // Validar preço atual com Sistema A
        ProductValidationResponse validation = systemAClient
                .validateProduct(item.getProductCode())
                .getBody();

        BigDecimal finalPrice = validation != null ?
                validation.getCurrentPrice() :
                item.getPrice();

        return finalPrice
                .multiply(BigDecimal.valueOf(item.getQuantity()))
                .setScale(2, RoundingMode.HALF_UP);
    }
}