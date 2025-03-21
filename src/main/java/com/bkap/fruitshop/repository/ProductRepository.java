package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductNameContainingIgnoreCase(String name);
    boolean existsByProductName(String productName);
}