package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
    boolean existsByTitleIgnoreCase(String title);
}