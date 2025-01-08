package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.CartItemRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.CartItemResponse;
import com.bkap.fruitshop.entity.Product;
import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;
import com.bkap.fruitshop.repository.ProductRepository;
import com.bkap.fruitshop.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartItemService cartItemService;
    private final ProductRepository productRepository;

    @GetMapping("/total-price")
    public Double getTotalPrice(@RequestParam("userId") long userId) {
        return cartItemService.calculateTotalPrice(userId);
    }

    @GetMapping("/count-cart-item")
    public long countCartItem(@RequestParam("userId") long userId) {
        return cartItemService.countItemsInCart(userId);
    }

    @GetMapping("/{userId}")
    public ApiResponse<List<CartItemResponse>> getCartByUserId(@PathVariable long userId) {
        return ApiResponse.<List<CartItemResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(cartItemService.findByUserId(userId))
                .build();
    }

    @PostMapping
    public ApiResponse<CartItemResponse> addToCart(@RequestBody CartItemRequest request) {
        Product p = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        CartItemResponse addCartItem = cartItemService.addCartItem(request);
        addCartItem.setPrice(p.getPrice());

        return ApiResponse.<CartItemResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .result(addCartItem)
                .build();
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ApiResponse<CartItemResponse> removeFromCart(@PathVariable long cartItemId) {
        cartItemService.removeCartItem(cartItemId);

        return ApiResponse.<CartItemResponse>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Cart item is removed successfully!")
                .build();
    }

    @DeleteMapping("/clear/{userId}")
    public ApiResponse<Void> clearCart(@PathVariable long userId) {
        cartItemService.clearCart(userId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Cart is deleted successfully!")
                .build();
    }

}
