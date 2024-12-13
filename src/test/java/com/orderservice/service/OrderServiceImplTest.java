package com.orderservice.service;

import com.orderservice.domain.Order;
import com.orderservice.domain.OrderItem;
import com.orderservice.domain.OrderStatus;
import com.orderservice.dto.OrderItemRequest;
import com.orderservice.dto.OrderRequest;
import com.orderservice.exception.OrderNotFoundException;
import com.orderservice.repository.OrderRepository;
import com.orderservice.service.processor.OrderProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderProcessor orderProcessor;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequest orderRequest;
    private Order order;

    @BeforeEach
    void setUp() {
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductCode("PROD-1");
        itemRequest.setQuantity(2);
        itemRequest.setPrice(BigDecimal.TEN);

        orderRequest = new OrderRequest();
        orderRequest.setItems(List.of(itemRequest));

        OrderItem orderItem = new OrderItem();
        orderItem.setProductCode("PROD-1");
        orderItem.setQuantity(2);
        orderItem.setPrice(BigDecimal.TEN);

        order = new Order();
        order.setId(1L);
        order.setOrderNumber("ORD-12345678");
        order.setItems(List.of(orderItem));
        order.setStatus(OrderStatus.RECEIVED);
        order.setTotalAmount(BigDecimal.valueOf(20));
    }

    @Test
    void createOrder_Success() {
        // Arrange
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        Order result = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.RECEIVED, result.getStatus());
        assertEquals("ORD-12345678", result.getOrderNumber());
        assertEquals(1, result.getItems().size());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getOrder_Success() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Act
        Order result = orderService.getOrder(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ORD-12345678", result.getOrderNumber());
    }

    @Test
    void getOrder_NotFound() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrder(1L));
    }

    @Test
    void processOrder_Success() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderProcessor.process(any(Order.class))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        Order result = orderService.processOrder(1L);

        // Assert
        assertNotNull(result);
        verify(orderProcessor).process(any(Order.class));
        verify(orderRepository, times(2)).save(any(Order.class));
    }
}