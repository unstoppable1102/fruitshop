package com.bkap.fruitshop.service;

import com.bkap.fruitshop.dto.request.WishlistRequest;
import com.bkap.fruitshop.dto.response.WishlistResponse;

import java.util.List;

public interface WishlistService {
    List<WishlistResponse> findWishlistsByUserId(long userId);
    WishlistResponse save(WishlistRequest request);
    void delete(long id);
}
