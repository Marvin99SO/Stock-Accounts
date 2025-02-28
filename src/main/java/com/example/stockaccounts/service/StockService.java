package com.example.stockaccounts.service;

import com.example.stockaccounts.dto.CreateStockDTO;
import com.example.stockaccounts.entity.Stock;
import com.example.stockaccounts.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void createStock(CreateStockDTO createStockDTO) {
        var stock = new Stock(
                createStockDTO.stockId(),
                createStockDTO.description()
        );
        stockRepository.save(stock);
    }
}
