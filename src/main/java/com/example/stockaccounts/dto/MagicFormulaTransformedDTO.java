package com.example.stockaccounts.dto;

import java.math.BigDecimal;

public record MagicFormulaTransformedDTO(
        String papel,
        BigDecimal cotacao,
        BigDecimal pl,
        BigDecimal evEbit,
        BigDecimal roic,
        BigDecimal liq2meses
) {
}
