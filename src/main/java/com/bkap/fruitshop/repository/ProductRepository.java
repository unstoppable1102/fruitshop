package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    boolean existsByProductName(String productName);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findByCategoryIdAndProductNameContainingIgnoreCase(Long categoryId, String keyword, Pageable pageable);
    Page<Product> findByCategoryIdAndPriceBetween(Long categoryId, Double minPrice, Double maxPrice, Pageable pageable);
    List<Product> findByCategoryIdAndIdNot(Long categoryId, Long id);
    List<Product> findTop8ByOrderByCreatedAtDesc();
}