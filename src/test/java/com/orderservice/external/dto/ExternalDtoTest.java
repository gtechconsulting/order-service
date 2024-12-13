package com.orderservice.external.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ExternalDtoTest {

    @Test
    void testProductValidationResponse() {
        ProductValidationResponse response = new ProductValidationResponse();
        response.setProductCode("PROD-1");
        response.setValid(true);
        response.setCurrentPrice(BigDecimal.TEN);
        response.setAvailableQuantity(100);

        assertEquals("PROD-1", response.getProductCode());
        assertTrue(response.isValid());
        assertEquals(BigDecimal.TEN, response.getCurrentPrice());
        assertEquals(100, response.getAvailableQuantity());
    }

    @Test
    void testCalculatedOrderRequest() {
        List<CalculatedOrderItem> items = List.of(
                CalculatedOrderItem.builder()
                        .productCode("PROD-1")
                        .quantity(2)
                        .price(BigDecimal.TEN)
                        .total(BigDecimal.valueOf(20))
                        .build()
        );

        CalculatedOrderRequest request = CalculatedOrderRequest.builder()
                .orderNumber("ORDER-123")
                .totalAmount(BigDecimal.valueOf(20))
                .items(items)
                .build();

        assertEquals("ORDER-123", request.getOrderNumber());
        assertEquals(BigDecimal.valueOf(20), request.getTotalAmount());
        assertEquals(1, request.getItems().size());
    }
}