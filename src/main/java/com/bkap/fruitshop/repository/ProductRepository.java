package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    boolean existsByProductName(String productName);
    List<Product> findTop8ByCategoryIdAndIdNot(Long categoryId, Long id);
    List<Product> findTop8ByOrderByCreatedAtDesc();

    @Query("SELECT p FROM Product p WHERE p.priceOld > p.price AND p.priceOld IS NOT NULL")
    List<Product> findByPriceOldGreaterThanPrice();
}