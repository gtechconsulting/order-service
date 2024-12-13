package com.orderservice.dto;


import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {
    private String productCode;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal total;
}
