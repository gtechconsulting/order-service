package com.orderservice.service.processor;

import com.orderservice.domain.Order;
import com.orderservice.domain.OrderItem;
import com.orderservice.domain.OrderStatus;
import com.orderservice.exception.OrderProcessingException;
import com.orderservice.external.client.SystemBClient;
import com.orderservice.service.calculation.OrderCalculationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderProcessorTest {

    @Mock
    private OrderCalculationStrategy calculationStrategy;

    @Mock
    private SystemBClient systemBClient;

    @InjectMocks
    private OrderProcessor orderProcessor;

    private Order order;

    @BeforeEach
    void setUp() {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductCode("PROD-1");
        orderItem.setQuantity(2);
        orderItem.setPrice(BigDecimal.TEN);

        order = new Order();
        order.setId(1L);
        order.setOrderNumber("ORD-12345678");
        order.setItems(List.of(orderItem));
        order.setStatus(OrderStatus.RECEIVED);
    }

    @Test
    void process_Success() {
        // Arrange
        when(calculationStrategy.calculate(any(Order.class)))
                .thenReturn(BigDecimal.valueOf(20));
        when(systemBClient.sendCalculatedOrder(any()))
                .thenReturn(ResponseEntity.ok().build());

        // Act
        Order result = orderProcessor.process(order);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.COMPLETED, result.getStatus());
        assertEquals(BigDecimal.valueOf(20), result.getTotalAmount());

        verify(calculationStrategy).calculate(order);
        verify(systemBClient).sendCalculatedOrder(any());
    }

    @Test
    void process_CalculationError() {
        // Arrange
        when(calculationStrategy.calculate(any(Order.class)))
                .thenThrow(new RuntimeException("Calculation error"));

        // Act & Assert
        assertThrows(OrderProcessingException.class, () -> orderProcessor.process(order));
        assertEquals(OrderStatus.ERROR, order.getStatus());
        verify(systemBClient, never()).sendCalculatedOrder(any());
    }
}