package com.ms_coffeeShop.service.serviceImplementation;

import com.ms_coffeeShop.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ms_coffeeShop.repository.ProductRepository;
import com.ms_coffeeShop.service.ProductService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(String productName) {
        Product product = new Product();
        //check if product name is empty or already exists
        if (productName.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (productRepository.existsByName(productName)) {
            throw new IllegalArgumentException("Product with name " + productName + " already exists");
        }
        product.setName(productName);
        return productRepository.save(product);
    }

    @Override
    public List<Product> findAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new NoSuchElementException("No products found");
        }
        return products;
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
    }

    @Override
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        } else {
            throw new NoSuchElementException("Product not found with id: " + id);
        }
    }
}