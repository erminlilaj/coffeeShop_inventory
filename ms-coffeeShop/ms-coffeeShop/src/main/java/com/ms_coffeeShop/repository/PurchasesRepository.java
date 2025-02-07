package com.ms_coffeeShop.repository;

import com.ms_coffeeShop.entity.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasesRepository extends JpaRepository<Purchases, Long> {
}
