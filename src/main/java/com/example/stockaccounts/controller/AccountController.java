package com.example.stockaccounts.controller;

import com.example.stockaccounts.dto.AccountStockDTO;
import com.example.stockaccounts.dto.AssociateAccountStockDTO;
import com.example.stockaccounts.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/stocks")
    public ResponseEntity<Void> associateStock(@PathVariable("accountId") String accountId,
                                                  @RequestBody AssociateAccountStockDTO associateDTO) {
        accountService.associateStock(accountId, associateDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/stocks")
    public ResponseEntity<List<AccountStockDTO>> associateStock(@PathVariable("accountId") String accountId) {
        var stocks = accountService.listStocks(accountId);
        return ResponseEntity.ok(stocks);
    }
}
