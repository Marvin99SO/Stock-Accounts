package com.example.stockaccounts.mapper;

import com.example.stockaccounts.dto.MagicFormulaDTO;
import com.example.stockaccounts.entity.MagicFormulaStock;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class MagicFormulaMapper {
    public MagicFormulaStock convertToModel(MagicFormulaDTO magicFormulaDTO) {
         return new MagicFormulaStock(
                magicFormulaDTO.papel(),
                convertStringToBigDecimal(magicFormulaDTO.cotacao()),
                convertStringToBigDecimal(magicFormulaDTO.pl()),
                convertStringToBigDecimal(magicFormulaDTO.evEbit()),
                convertStringToBigDecimalPercent(magicFormulaDTO.roic()),
                convertStringToBigDecimal(magicFormulaDTO.liq2meses())
        );
    }

    private BigDecimal convertStringToBigDecimal(String value) {
        BigDecimal bd;
        try {
            bd = new BigDecimal(textTransform(value)).setScale(2);
        } catch (NumberFormatException e) {
            bd = null;
        }
        return bd;
    }

    private BigDecimal convertStringToBigDecimalPercent(String value) {
        BigDecimal bd;
        try {
            bd = new BigDecimal(textTransform(value).replace("%",""))
                    .setScale(4).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            bd = null;
        }
        return bd;
    }

    private static String textTransform(String text) {
        return text.replace(".", "").replace(",", ".");
    }
}
