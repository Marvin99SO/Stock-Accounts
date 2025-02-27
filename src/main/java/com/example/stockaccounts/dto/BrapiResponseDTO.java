package com.example.stockaccounts.dto;

import java.util.List;

public record BrapiResponseDTO (List<StockDTO> results) {
}
