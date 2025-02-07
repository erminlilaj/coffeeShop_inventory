package com.ms_coffeeShop.service.serviceImplementation;

import com.ms_coffeeShop.DTOs.TransactionDto;
import com.ms_coffeeShop.entity.Product;
import com.ms_coffeeShop.entity.Purchases;
import com.ms_coffeeShop.entity.Sellings;
import com.ms_coffeeShop.repository.PurchasesRepository;
import com.ms_coffeeShop.repository.SellingsRepository;
import com.ms_coffeeShop.service.ProductService;
import com.ms_coffeeShop.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final PurchasesRepository purchasesRepository;
    private final SellingsRepository sellingsRepository;
    private final ProductService productService;
    @Override
    public Purchases createPurchase(TransactionDto transactionDto) {
        Long productId = transactionDto.getProductId();
        Product product = productService.getProductById(productId);
        productService.updateProductPurchases(product,transactionDto.getQuantity(),transactionDto.getPrice());
        Purchases purchases = new Purchases();
        purchases.setProduct(product);
        purchases.setQuantity(transactionDto.getQuantity());
        purchases.setPrice(transactionDto.getPrice());
        purchases.setTotalPrice(transactionDto.getPrice()*transactionDto.getQuantity());
        purchases.setBuyingDate(transactionDto.getTransactionDate());
        return purchasesRepository.save(purchases);


    }

    @Override
    public Sellings createSell(TransactionDto transactionDto) {
        Long productId = transactionDto.getProductId();
        Product product = productService.getProductById(productId);

        if(product.getCurrentStock()<transactionDto.getQuantity()){
            throw new IllegalArgumentException("Not enough products in stock to sell");
        }
        productService.updateProductSellings(product,transactionDto.getQuantity(),transactionDto.getPrice());
        Sellings sellings = new Sellings();
        sellings.setProduct(product);
        sellings.setQuantity(transactionDto.getQuantity());
        sellings.setPrice(transactionDto.getPrice());
        sellings.setTotalPrice(transactionDto.getPrice()*transactionDto.getQuantity());
        sellings.setSellingDate(transactionDto.getTransactionDate());
        return sellingsRepository.save(sellings);
    }

    @Override
    public Page<Purchases> getAllPurchases(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return purchasesRepository.findAll(pageable);

    }

    @Override
    public Page<Sellings> getAllSellings(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return sellingsRepository.findAll(pageable);
    }

}
