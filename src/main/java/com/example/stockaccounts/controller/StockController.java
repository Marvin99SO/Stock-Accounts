package com.example.stockaccounts.controller;

import com.example.stockaccounts.dto.CreateStockDTO;
import com.example.stockaccounts.dto.CreateUserDTO;
import com.example.stockaccounts.entity.Stock;
import com.example.stockaccounts.entity.User;
import com.example.stockaccounts.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/v1/stocks")
public class StockController {
    private StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<Void> createStock(@RequestBody CreateStockDTO createStockDTO) {
        stockService.createStock(createStockDTO);
        return ResponseEntity.ok().build();
    }
}
