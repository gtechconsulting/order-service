package com.orderservice.service.calculation;

import com.orderservice.domain.Order;
import java.math.BigDecimal;

public interface OrderCalculationStrategy {
    BigDecimal calculate(Order order);
}