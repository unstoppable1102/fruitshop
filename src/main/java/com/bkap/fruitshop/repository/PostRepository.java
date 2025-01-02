package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    boolean existsByPostCategoryId(Long postCategoryId);
    List<Post> findByTitleContainingIgnoreCase(String title);
    boolean existsByTitleIgnoreCase(String title);
}