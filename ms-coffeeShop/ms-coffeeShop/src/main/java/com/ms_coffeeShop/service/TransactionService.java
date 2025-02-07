package com.ms_coffeeShop.service;

import com.ms_coffeeShop.DTOs.TransactionDto;
import com.ms_coffeeShop.entity.Purchases;
import com.ms_coffeeShop.entity.Sellings;
import org.springframework.data.domain.Page;

public interface TransactionService {
    Purchases createPurchase(TransactionDto transactionDto);

    Sellings createSell(TransactionDto transactionDto);

    Page<Purchases> getAllPurchases(int page, int size);

    Page<Sellings> getAllSellings(int page, int size);
}
