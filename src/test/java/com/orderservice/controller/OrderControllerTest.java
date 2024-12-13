package com.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.domain.Order;
import com.orderservice.dto.OrderItemRequest;
import com.orderservice.dto.OrderRequest;
import com.orderservice.exception.OrderNotFoundException;
import com.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderRequest createOrderRequest() {
        OrderItemRequest item = new OrderItemRequest();
        item.setProductCode("PROD-1");
        item.setQuantity(2);
        item.setPrice(BigDecimal.TEN);

        OrderRequest request = new OrderRequest();
        request.setItems(List.of(item));
        return request;
    }

    @Test
    void createOrder_Success() throws Exception {
        OrderRequest request = createOrderRequest();
        Order order = new Order();
        order.setId(1L);
        order.setOrderNumber("ORDER-123");

        when(orderService.createOrder(any())).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderNumber").value("ORDER-123"));
    }

    @Test
    void getOrder_Success() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setOrderNumber("ORDER-123");

        when(orderService.getOrder(1L)).thenReturn(order);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderNumber").value("ORDER-123"));
    }

    @Test
    void getOrder_NotFound() throws Exception {
        when(orderService.getOrder(1L)).thenThrow(new OrderNotFoundException("Order not found"));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order not found"));
    }

    @Test
    void getAllOrders_Success() throws Exception {
        List<Order> orders = List.of(
                createOrder(1L, "ORDER-123"),
                createOrder(2L, "ORDER-456")
        );

        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    private Order createOrder(Long id, String orderNumber) {
        Order order = new Order();
        order.setId(id);
        order.setOrderNumber(orderNumber);
        return order;
    }
}