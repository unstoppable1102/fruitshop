package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.Cart;
import com.bkap.fruitshop.entity.CartItem;
import com.bkap.fruitshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    int countByCartUserId(Long userId);
}