package com.orderservice.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemRequest {
    private String productCode;
    private Integer quantity;
    private BigDecimal price;
}