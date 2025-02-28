package com.example.stockaccounts.controller;

import com.example.stockaccounts.dto.MagicFormulaTransformedDTO;
import com.example.stockaccounts.entity.MagicFormulaStock;
import com.example.stockaccounts.service.MagicFormulaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/formula")
public class MagicFormulaController {
    private static final Logger log = LoggerFactory.getLogger(MagicFormulaController.class);
    private MagicFormulaService magicFormulaService;

    public MagicFormulaController(MagicFormulaService magicFormulaService) {
        this.magicFormulaService = magicFormulaService;
    }

    @GetMapping
    public ResponseEntity<List<MagicFormulaStock>> getMagicFormulaStocks() throws IOException {
        try {
            return ResponseEntity.ok(magicFormulaService.getMagicFormulaStocks());
        } catch (Exception e) {
            log.error("Error while retrieving stocks from magic formula", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
