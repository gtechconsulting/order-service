package com.orderservice.service;

import com.orderservice.domain.Order;
import com.orderservice.domain.OrderItem;
import com.orderservice.domain.OrderStatus;
import com.orderservice.dto.OrderRequest;
import com.orderservice.exception.OrderNotFoundException;
import com.orderservice.exception.OrderProcessingException;
import com.orderservice.repository.OrderRepository;
import com.orderservice.service.processor.OrderProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderProcessor orderProcessor;

    @Override
    @Transactional
    public Order createOrder(OrderRequest request) {
        log.info("Creating new order");

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.RECEIVED);

        List<OrderItem> items = request.getItems().stream()
                .map(itemRequest -> {
                    OrderItem item = new OrderItem();
                    item.setProductCode(itemRequest.getProductCode());
                    item.setQuantity(itemRequest.getQuantity());
                    item.setPrice(itemRequest.getPrice());
                    return item;
                })
                .collect(Collectors.toList());

        order.setItems(items);
        order.setTotalAmount(calculateTotal(items));

        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with number: {}", savedOrder.getOrderNumber());

        return savedOrder;
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public Order processOrder(Long id) {
        Order order = getOrder(id);

        if (order.getStatus() != OrderStatus.RECEIVED) {
            throw new OrderProcessingException("Order is not in RECEIVED status");
        }

        order.setStatus(OrderStatus.PROCESSING);
        order = orderRepository.save(order);

        Order processedOrder = orderProcessor.process(order);
        return orderRepository.save(processedOrder);
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}