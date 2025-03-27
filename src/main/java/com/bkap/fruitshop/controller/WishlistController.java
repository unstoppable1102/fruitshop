package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.WishlistRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.WishlistResponse;
import com.bkap.fruitshop.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping("/user/{userId}")
    public ApiResponse<List<WishlistResponse>> getWishlistsByUserId(@PathVariable("userId") long userId) {
        try {
            return ApiResponse.<List<WishlistResponse>>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(wishlistService.findWishlistsByUserId(userId))
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping
    public ApiResponse<WishlistResponse> create(@RequestBody WishlistRequest request) {
        try {
            return ApiResponse.<WishlistResponse>builder()
                    .code(HttpStatus.CREATED.value())
                    .message(HttpStatus.CREATED.getReasonPhrase())
                    .result(wishlistService.save(request))
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @DeleteMapping("/remove/{id}")
    public ApiResponse<Void> remove(@PathVariable int id) {
        try {
            wishlistService.delete(id);
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("Item is deleted from Wishlist")
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
