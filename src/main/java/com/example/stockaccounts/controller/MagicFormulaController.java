package com.example.stockaccounts.controller;

import com.example.stockaccounts.entity.MagicFormulaStock;
import com.example.stockaccounts.service.MagicFormulaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/formula")
public class MagicFormulaController {
    private static final Logger log = LoggerFactory.getLogger(MagicFormulaController.class);
    private final MagicFormulaService magicFormulaService;

    public MagicFormulaController(MagicFormulaService magicFormulaService) {
        this.magicFormulaService = magicFormulaService;
    }

    @GetMapping
    public ResponseEntity<List<MagicFormulaStock>> getMagicFormulaStocks() throws IOException {
        return ResponseEntity.ok(magicFormulaService.getMagicFormulaStocks());
    }
}
