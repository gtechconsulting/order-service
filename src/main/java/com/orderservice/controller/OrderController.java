package com.orderservice.controller;

import com.orderservice.domain.Order;
import com.orderservice.dto.OrderRequest;
import com.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Orders", description = "API para gerenciamento de pedidos")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Criar novo pedido", description = "Cria um novo pedido com os itens especificados")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        log.info("Received request to create order");
        Order order = orderService.createOrder(request);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico baseado no ID")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        log.info("Received request to get order with id: {}", id);
        Order order = orderService.getOrder(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    @Operation(summary = "Listar todos pedidos", description = "Retorna uma lista de todos os pedidos")
    public ResponseEntity<List<Order>> getAllOrders() {
        log.info("Received request to list all orders");
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{id}/process")
    @Operation(summary = "Processar pedido", description = "Processa um pedido específico")
    public ResponseEntity<Order> processOrder(@PathVariable Long id) {
        log.info("Received request to process order with id: {}", id);
        Order order = orderService.processOrder(id);
        return ResponseEntity.ok(order);
    }
}