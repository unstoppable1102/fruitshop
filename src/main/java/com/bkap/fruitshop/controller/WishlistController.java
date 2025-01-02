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

    @GetMapping("/{userId}")
    public ApiResponse<List<WishlistResponse>> getWishlistsByUserId(@PathVariable("userId") long userId) {

        List<WishlistResponse> responses = wishlistService.findWishlistsByUserId(userId);


        return ApiResponse.<List<WishlistResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(responses)
                .build();
    }

    @PostMapping
    public ApiResponse<WishlistResponse> create(@RequestBody WishlistRequest request) {
        WishlistResponse response = wishlistService.save(request);

        return ApiResponse.<WishlistResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .result(response)
                .build();
    }

    @DeleteMapping("/remove/{id}")
    public ApiResponse<Void> remove(@PathVariable int id) {
        wishlistService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Item is deleted from Wishlist")
                .build();
    }
}
