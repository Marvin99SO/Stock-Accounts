package com.example.stockaccounts.repository;

import com.example.stockaccounts.entity.AccountStock;
import com.example.stockaccounts.entity.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}
