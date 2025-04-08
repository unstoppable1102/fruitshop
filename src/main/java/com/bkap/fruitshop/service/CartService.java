package com.bkap.fruitshop.service;

import com.bkap.fruitshop.dto.request.CartRequest;
import com.bkap.fruitshop.dto.response.CartResponse;

import java.util.List;

public interface CartService {

    CartResponse addToCart(CartRequest request);
    List<CartResponse> getCartsByUserId(Long userId);
    CartResponse getCartById(Long cartId);
    void removeFromCart(Long userId,  Long productId);
    void clearCart(Long userId);
    int countCartItem(Long userId);
    double getTotalPrice(Long userId);

}
