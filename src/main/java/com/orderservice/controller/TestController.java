package com.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test", description = "API de teste")
public class TestController {

    @Operation(summary = "Teste da API")
    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("API is working!");
    }
}