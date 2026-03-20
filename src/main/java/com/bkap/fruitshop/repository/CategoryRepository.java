package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  List<Category> findByNameContainingIgnoreCase(String name);
  boolean existsByNameIgnoreCase(String name);
}