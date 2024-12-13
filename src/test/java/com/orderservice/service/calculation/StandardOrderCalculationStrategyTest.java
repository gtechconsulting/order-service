package com.orderservice.service.calculation;

import com.orderservice.domain.Order;
import com.orderservice.domain.OrderItem;
import com.orderservice.external.client.SystemAClient;
import com.orderservice.external.dto.ProductValidationResponse;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StandardOrderCalculationStrategyTest {

    @Mock
    private SystemAClient systemAClient;

    @InjectMocks
    private StandardOrderCalculationStrategy calculationStrategy;

    private Order order;
    private ProductValidationResponse validationResponse;

    @BeforeEach
    void setUp() {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductCode("PROD-1");
        orderItem.setQuantity(2);
        orderItem.setPrice(BigDecimal.TEN);

        order = new Order();
        order.setItems(List.of(orderItem));

        validationResponse = new ProductValidationResponse();
        validationResponse.setProductCode("PROD-1");
        validationResponse.setValid(true);
        validationResponse.setCurrentPrice(BigDecimal.TEN);
    }

    @Test
    void calculate_Success() {
        // Arrange
        when(systemAClient.validateProduct(anyString()))
                .thenReturn(ResponseEntity.ok(validationResponse));

        // Act
        BigDecimal result = calculationStrategy.calculate(order);

        // Assert
        assertEquals(0, result.compareTo(BigDecimal.valueOf(20.00)),
                "Expected 20.00 but got " + result);
    }

    @Test
    void calculate_WithUpdatedPrice() {
        // Arrange
        validationResponse.setCurrentPrice(BigDecimal.valueOf(15));
        when(systemAClient.validateProduct(anyString()))
                .thenReturn(ResponseEntity.ok(validationResponse));

        // Act
        BigDecimal result = calculationStrategy.calculate(order);

        // Assert
        assertEquals(0, result.compareTo(BigDecimal.valueOf(30.00)),
                "Expected 30.00 but got " + result);
    }
}