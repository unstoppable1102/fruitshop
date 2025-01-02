package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(long userId);

    long countCartItemsByUserId(long userId);

    CartItem findByUserIdAndProductId(long userId, long productId);

}