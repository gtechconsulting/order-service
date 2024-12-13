package com.orderservice.external.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductValidationResponse {
    private String productCode;
    private boolean valid;
    private BigDecimal currentPrice;
    private Integer availableQuantity;
}