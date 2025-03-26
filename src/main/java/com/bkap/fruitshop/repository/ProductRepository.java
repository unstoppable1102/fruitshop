package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByProductNameContainingIgnoreCase(String name, Pageable pageable);
    boolean existsByProductName(String productName);
}