package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
   Optional<Cart> findByUserId(@Param("userId") long userId);
}