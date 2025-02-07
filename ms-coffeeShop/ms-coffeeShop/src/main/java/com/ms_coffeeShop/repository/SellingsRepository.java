package com.ms_coffeeShop.repository;

import com.ms_coffeeShop.entity.Sellings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.YearMonth;
import java.util.List;

public interface SellingsRepository extends JpaRepository<Sellings, Long> {


    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM Sellings s WHERE s.product.id = :productId AND YEAR(s.sellingDate) = :#{#month.year} AND MONTH(s.sellingDate) = :#{#month.monthValue}")
    Integer sumQuantityByProductAndMonth(Long productId, YearMonth month);

    @Query("SELECT COALESCE(SUM(s.totalPrice), 0.0) FROM Sellings s WHERE s.product.id = :productId AND YEAR(s.sellingDate) = :#{#month.year} AND MONTH(s.sellingDate) = :#{#month.monthValue}")
    Double sumTotalPriceByProductAndMonth(Long productId, YearMonth month);

    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM Sellings s WHERE s.product.id = :productId AND YEAR(s.sellingDate) = :year")
    Integer sumQuantityByProductAndYear(Long productId, int year);

    @Query("SELECT COALESCE(SUM(s.totalPrice), 0.0) FROM Sellings s WHERE s.product.id = :productId AND YEAR(s.sellingDate) = :year")
    Double sumTotalPriceByProductAndYear(Long productId, int year);

    @Query("SELECT DISTINCT s.product.id FROM Sellings s WHERE YEAR(s.sellingDate) = :year")
    List<Long> findProductIdsWithSellingsInYear(int year);

}
