package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.PostRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.PageResponse;
import com.bkap.fruitshop.dto.response.PostResponse;
import com.bkap.fruitshop.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ApiResponse<PageResponse<PostResponse>> getAllPosts(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 2, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        return ApiResponse.<PageResponse<PostResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(postService.findAll(keyword, pageable))
                .build();
    }

    @PostMapping
    public ApiResponse<PostResponse> createPost(@Valid @ModelAttribute PostRequest request, BindingResult result){
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), String.valueOf(errorMessages));
        }
            return ApiResponse.<PostResponse>builder()
                    .code(HttpStatus.CREATED.value())
                    .message(HttpStatus.CREATED.getReasonPhrase())
                    .result(postService.create(request))
                    .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PostResponse> getPostById(@PathVariable Long id){
        try {
            return ApiResponse.<PostResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(postService.findById(id))
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<PostResponse> updatePost(@Valid @PathVariable Long id, @ModelAttribute PostRequest request, BindingResult result){
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), String.valueOf(errorMessages));
        }
            return ApiResponse.<PostResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(postService.update(id, request))
                    .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePost(@PathVariable Long id){
        try {
            postService.delete(id);
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("Post is deleted successfully!")
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }
}
