package com.ms_coffeeShop.service;

import com.ms_coffeeShop.entity.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(String productName);

    List<Product> findAllProducts();

    Product getProductById(Long id);

    boolean deleteProduct(Long id);
}
