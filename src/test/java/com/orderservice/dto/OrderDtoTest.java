package com.orderservice.dto;

import com.orderservice.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OrderDtoTest {

    @Test
    void testOrderRequest() {
        OrderItemRequest item = new OrderItemRequest();
        item.setProductCode("PROD-1");
        item.setQuantity(2);
        item.setPrice(BigDecimal.TEN);

        OrderRequest request = new OrderRequest();
        request.setItems(List.of(item));

        assertEquals(1, request.getItems().size());
        assertEquals("PROD-1", request.getItems().get(0).getProductCode());
        assertEquals(2, request.getItems().get(0).getQuantity());
        assertEquals(BigDecimal.TEN, request.getItems().get(0).getPrice());
    }

    @Test
    void testOrderItemResponse() {
        OrderItemResponse response = OrderItemResponse.builder()
                .productCode("PROD-1")
                .quantity(2)
                .price(BigDecimal.TEN)
                .total(BigDecimal.valueOf(20))
                .build();

        assertEquals("PROD-1", response.getProductCode());
        assertEquals(Integer.valueOf(2), response.getQuantity());
        assertEquals(BigDecimal.TEN, response.getPrice());
        assertEquals(BigDecimal.valueOf(20), response.getTotal());
    }

    @Test
    void testOrderResponse() {
        LocalDateTime now = LocalDateTime.now();
        OrderItemResponse item = OrderItemResponse.builder()
                .productCode("PROD-1")
                .quantity(2)
                .price(BigDecimal.TEN)
                .total(BigDecimal.valueOf(20))
                .build();

        OrderResponse response = OrderResponse.builder()
                .id(1L)
                .orderNumber("ORDER-123")
                .status(OrderStatus.RECEIVED)
                .totalAmount(BigDecimal.valueOf(20))
                .items(List.of(item))
                .createdAt(now)
                .build();

        assertEquals(Long.valueOf(1L), response.getId());
        assertEquals("ORDER-123", response.getOrderNumber());
        assertEquals(OrderStatus.RECEIVED, response.getStatus());
        assertEquals(BigDecimal.valueOf(20), response.getTotalAmount());
        assertEquals(1, response.getItems().size());
        assertEquals(now, response.getCreatedAt());
    }
}