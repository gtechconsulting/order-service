package com.orderservice.external.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CalculatedOrderRequest {
    private String orderNumber;
    private BigDecimal totalAmount;
    private List<CalculatedOrderItem> items;
}