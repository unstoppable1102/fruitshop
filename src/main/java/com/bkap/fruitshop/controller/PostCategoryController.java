package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.PostCategoryRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.PostCategoryResponse;
import com.bkap.fruitshop.service.PostCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post-categories")
public class PostCategoryController {

    private final PostCategoryService postCategoryService;

    @GetMapping
    public ApiResponse<List<PostCategoryResponse>> getAllPostCategories() {
        return ApiResponse.<List<PostCategoryResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(postCategoryService.findAll())
                .build();
    }

    @PostMapping
    public ApiResponse<PostCategoryResponse> createPostCategory(@RequestBody PostCategoryRequest request) {
        try {
            return ApiResponse.<PostCategoryResponse>builder()
                    .code(HttpStatus.CREATED.value())
                    .message(HttpStatus.CREATED.getReasonPhrase())
                    .result(postCategoryService.create(request))
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<PostCategoryResponse> getPostCategoryById(@PathVariable Long id) {
        try {
            return ApiResponse.<PostCategoryResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(postCategoryService.findById(id))
                    .build();
        }catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/search/{name}")
    public ApiResponse<List<PostCategoryResponse>> getByName(@PathVariable String name) {
        try {
            return ApiResponse.<List<PostCategoryResponse>>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(postCategoryService.findByName(name))
                    .build();
        }catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<PostCategoryResponse> updatePostCategory(@PathVariable Long id, @RequestBody PostCategoryRequest request) {
        try {
            return ApiResponse.<PostCategoryResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(postCategoryService.update(id, request))
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePostCategory(@PathVariable Long id) {
        try {
            postCategoryService.delete(id);
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("Post Category is deleted successfully!")
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }
}
