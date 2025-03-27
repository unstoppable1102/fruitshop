package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.CartRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.CartItemResponse;
import com.bkap.fruitshop.dto.response.CartResponse;
import com.bkap.fruitshop.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping("/total-price")
    public ApiResponse<Double> getTotalPrice(@RequestParam("userId") long userId) {
        return ApiResponse.<Double>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(cartService.getTotalPrice(userId))
                .build();
    }

    @GetMapping("/count-cart-item")
    public ApiResponse<Integer> countCartItem(@RequestParam("userId") long userId) {
        return ApiResponse.<Integer>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(cartService.countCartItem(userId))
                .build();
    }

    @GetMapping("/{cartId}")
    public ApiResponse<CartResponse> getCartById(@PathVariable long cartId) {
        try {
            return ApiResponse.<CartResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(cartService.getCartById(cartId))
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<CartResponse>> getCartByUserId(@PathVariable long userId) {
        try {
            return ApiResponse.<List<CartResponse>>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(cartService.getCartsByUserId(userId))
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping
    public ApiResponse<CartResponse> addToCart(@Valid @RequestBody CartRequest request, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), String.valueOf(errorMessages));
        }
            return ApiResponse.<CartResponse>builder()
                    .code(HttpStatus.CREATED.value())
                    .message(HttpStatus.CREATED.getReasonPhrase())
                    .result(cartService.addToCart(request))
                    .build();
    }

    @DeleteMapping("/remove")
    public ApiResponse<CartItemResponse> removeFromCart(@RequestParam Long userId, @RequestParam Long productId) {
        cartService.removeFromCart(userId, productId);
        try {
            return ApiResponse.<CartItemResponse>builder()
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("Cart item is removed successfully!")
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public ApiResponse<Void> clearCart(@RequestParam long userId) {
        cartService.clearCart(userId);
        try {
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("Cart is deleted successfully!")
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
