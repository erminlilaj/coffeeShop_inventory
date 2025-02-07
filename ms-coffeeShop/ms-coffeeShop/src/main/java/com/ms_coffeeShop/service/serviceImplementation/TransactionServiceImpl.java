package com.ms_coffeeShop.service.serviceImplementation;

import com.ms_coffeeShop.DTOs.MonthlyStatisticsDto;
import com.ms_coffeeShop.DTOs.TransactionDto;
import com.ms_coffeeShop.DTOs.YearlyStatisticsDTO;
import com.ms_coffeeShop.entity.Product;
import com.ms_coffeeShop.entity.Purchases;
import com.ms_coffeeShop.entity.Sellings;
import com.ms_coffeeShop.repository.ProductRepository;
import com.ms_coffeeShop.repository.PurchasesRepository;
import com.ms_coffeeShop.repository.SellingsRepository;
import com.ms_coffeeShop.service.ProductService;
import com.ms_coffeeShop.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final PurchasesRepository purchasesRepository;
    private final SellingsRepository sellingsRepository;
    private final ProductRepository productRepository;
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

    @Override
    public List<MonthlyStatisticsDto> getMonthlyStatistics(YearMonth month) {
        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> {
            // Using primitive types to avoid null pointer issues
            int totalBought = purchasesRepository.sumQuantityByProductAndMonth(product.getId(), month);
            double totalSpent = purchasesRepository.sumTotalPriceByProductAndMonth(product.getId(), month);
            int totalSold = sellingsRepository.sumQuantityByProductAndMonth(product.getId(), month);
            double totalRevenue = sellingsRepository.sumTotalPriceByProductAndMonth(product.getId(), month);
            double totalProfit = totalRevenue - totalSpent;
            int stockAtEndOfMonth = product.getCurrentStock();

            return new MonthlyStatisticsDto(
                    product.getName(),
                    totalBought,
                    totalSpent,
                    totalSold,
                    totalProfit,
                    stockAtEndOfMonth
            );
        }).collect(Collectors.toList());
    }

    @Override
    public List<YearlyStatisticsDTO> getYearlyStatistics(int year) {

        List<Long> purchaseProductIds = purchasesRepository.findProductIdsWithPurchasesInYear(year);
        List<Long> sellingProductIds = sellingsRepository.findProductIdsWithSellingsInYear(year);


        Set<Long> activeProductIds = new HashSet<>();
        activeProductIds.addAll(purchaseProductIds);
        activeProductIds.addAll(sellingProductIds);


        List<Product> activeProducts = productRepository.findAllById(activeProductIds);

        return activeProducts.stream().map(product -> {
            int totalPurchased = purchasesRepository.sumQuantityByProductAndYear(product.getId(), year);
            double totalPurchaseCost = purchasesRepository.sumTotalPriceByProductAndYear(product.getId(), year);
            int totalSold = sellingsRepository.sumQuantityByProductAndYear(product.getId(), year);
            double totalSalesRevenue = sellingsRepository.sumTotalPriceByProductAndYear(product.getId(), year);
            double yearlyProfit = totalSalesRevenue - totalPurchaseCost;

            return new YearlyStatisticsDTO(
                    product.getName(),
                    totalPurchased,
                    totalPurchaseCost,
                    totalSold,
                    totalSalesRevenue,
                    yearlyProfit,
                    product.getCurrentStock()
            );
        }).collect(Collectors.toList());
    }

}
