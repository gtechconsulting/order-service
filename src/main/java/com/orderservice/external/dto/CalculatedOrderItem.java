package com.orderservice.external.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class CalculatedOrderItem {
    private String productCode;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal total;
}