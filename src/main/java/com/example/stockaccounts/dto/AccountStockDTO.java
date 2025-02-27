package com.example.stockaccounts.dto;

import java.math.BigDecimal;

public record AccountStockDTO(String stockId, int quantity, BigDecimal total) {
}
