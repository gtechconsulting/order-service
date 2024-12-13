package com.orderservice.service;

import com.orderservice.domain.Order;
import com.orderservice.dto.OrderRequest;
import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequest request);
    Order getOrder(Long id);
    List<Order> getAllOrders();
    Order processOrder(Long id);
}