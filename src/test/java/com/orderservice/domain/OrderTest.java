package com.orderservice.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    @Test
    void testOrderCreation() {
        Order order = new Order();
        order.setOrderNumber("ORDER-123");
        order.setStatus(OrderStatus.RECEIVED);
        order.setTotalAmount(BigDecimal.valueOf(100));

        List<OrderItem> items = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setProductCode("PROD-1");
        item.setQuantity(2);
        item.setPrice(BigDecimal.valueOf(50));
        items.add(item);

        order.setItems(items);

        assertEquals("ORDER-123", order.getOrderNumber());
        assertEquals(OrderStatus.RECEIVED, order.getStatus());
        assertEquals(BigDecimal.valueOf(100), order.getTotalAmount());
        assertEquals(1, order.getItems().size());
    }

    @Test
    void testPrePersistAndPreUpdate() throws InterruptedException {
        Order order = new Order();
        assertNull(order.getCreatedAt());
        assertNull(order.getUpdatedAt());

        order.onCreate();
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());

        LocalDateTime created = order.getCreatedAt();
        Thread.sleep(1);

        order.onUpdate();
        assertEquals(created, order.getCreatedAt());
        assertTrue(order.getUpdatedAt().isAfter(created));
    }
}