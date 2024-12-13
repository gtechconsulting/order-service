package com.orderservice.controller;

import com.orderservice.domain.Order;
import com.orderservice.dto.OrderRequest;
import com.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "API para gerenciamento de pedidos")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Criar novo pedido")
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @Operation(summary = "Buscar pedido por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @Operation(summary = "Listar todos os pedidos")
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @Operation(summary = "Processar pedido")
    @PostMapping("/{id}/process")
    public ResponseEntity<Order> processOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.processOrder(id));
    }
}