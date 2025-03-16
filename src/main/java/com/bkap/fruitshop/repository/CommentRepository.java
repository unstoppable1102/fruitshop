package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    List<Comment> findByUserIdAndPostId(Long userId, Long postId);
    long countByPostId(Long postId);
}