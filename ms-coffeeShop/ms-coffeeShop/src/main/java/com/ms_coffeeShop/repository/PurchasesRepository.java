package com.ms_coffeeShop.repository;

import com.ms_coffeeShop.entity.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.YearMonth;
import java.util.List;

public interface PurchasesRepository extends JpaRepository<Purchases, Long> {


    @Query("SELECT COALESCE(SUM(p.quantity), 0) FROM Purchases p WHERE p.product.id = :productId AND YEAR(p.buyingDate) = :#{#month.year} AND MONTH(p.buyingDate) = :#{#month.monthValue}")
    Integer sumQuantityByProductAndMonth(Long productId, YearMonth month);

    @Query("SELECT COALESCE(SUM(p.totalPrice), 0.0) FROM Purchases p WHERE p.product.id = :productId AND YEAR(p.buyingDate) = :#{#month.year} AND MONTH(p.buyingDate) = :#{#month.monthValue}")
    Double sumTotalPriceByProductAndMonth(Long productId, YearMonth month);

    @Query("SELECT COALESCE(SUM(p.quantity), 0) FROM Purchases p WHERE p.product.id = :productId AND YEAR(p.buyingDate) = :year")
    Integer sumQuantityByProductAndYear(Long productId, int year);

    @Query("SELECT COALESCE(SUM(p.totalPrice), 0.0) FROM Purchases p WHERE p.product.id = :productId AND YEAR(p.buyingDate) = :year")
    Double sumTotalPriceByProductAndYear(Long productId, int year);

    @Query("SELECT DISTINCT p.product.id FROM Purchases p WHERE YEAR(p.buyingDate) = :year")
    List<Long> findProductIdsWithPurchasesInYear(int year);
}
