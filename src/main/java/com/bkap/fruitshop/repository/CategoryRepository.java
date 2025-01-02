package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  List<Category> findByNameContainingIgnoreCase(String name);
  boolean existsByName(String name);
}