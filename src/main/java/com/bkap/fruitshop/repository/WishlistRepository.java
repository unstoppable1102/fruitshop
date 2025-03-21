package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findWishlistsByUserId(long userId);
    Optional<Wishlist> findByUserIdAndProductId(long userId, long productId);
}