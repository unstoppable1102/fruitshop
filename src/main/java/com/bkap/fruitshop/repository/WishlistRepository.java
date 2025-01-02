package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findWishlistsByUserId(long userId);
    Wishlist findByUserIdAndProductId(long userId, long productId);
}