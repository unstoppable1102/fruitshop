package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
    boolean existsByName(String name);
    List<PostCategory> findByNameContainingIgnoreCase(String name);
}