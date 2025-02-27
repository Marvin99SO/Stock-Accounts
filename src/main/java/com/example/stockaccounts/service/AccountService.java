package com.example.stockaccounts.service;

import com.example.stockaccounts.client.BrapiClient;
import com.example.stockaccounts.dto.AccountStockDTO;
import com.example.stockaccounts.dto.AssociateAccountStockDTO;
import com.example.stockaccounts.entity.AccountStock;
import com.example.stockaccounts.entity.AccountStockId;
import com.example.stockaccounts.repository.AccountRepository;
import com.example.stockaccounts.repository.AccountStockRepository;
import com.example.stockaccounts.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    @Value("#{environment.TOKEN}")
    private String TOKEN;

    private AccountRepository accountRepository;
    private StockRepository stockRepository;
    private AccountStockRepository accountStockRepository;
    private BrapiClient brapiClient;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository, BrapiClient brapiClient) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
        this.brapiClient = brapiClient;
    }

    public void associateStock(String accountId, AssociateAccountStockDTO associateDTO) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var stock = stockRepository.findById(associateDTO.stockId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        //DTO -> ENTITY
        var id = new AccountStockId(account.getAccountId(), stock.getStockId());
        var entity = new AccountStock(
                id,
                account,
                stock,
                associateDTO.quantity()
        );

        accountStockRepository.save(entity);
    }

    public List<AccountStockDTO> listStocks(String accountId) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return account.getAccountStocks()
                .stream()
                .map(accountStock ->
                        new AccountStockDTO(
                                accountStock.getStock().getStockId(),
                                accountStock.getQuantity(), getTotal(
                                        accountStock.getStock().getStockId(),
                                        accountStock.getQuantity())
                        ))
                .toList();
    }

    private BigDecimal getTotal(String stockId, Integer quantity) {
        var response = brapiClient.getQuote(TOKEN, stockId);
        var price = response.results().getFirst().regularMarketPrice();
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
