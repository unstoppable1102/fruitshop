package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.PostRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.PostResponse;
import com.bkap.fruitshop.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ApiResponse<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.findAll();
        return ApiResponse.<List<PostResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(posts)
                .build();
    }

    @PostMapping
    public ApiResponse<PostResponse> createPost(@ModelAttribute PostRequest request){
        try {
            PostResponse response = postService.create(request);
            return ApiResponse.<PostResponse>builder()
                    .code(HttpStatus.CREATED.value())
                    .message(HttpStatus.CREATED.getReasonPhrase())
                    .result(response)
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<PostResponse> getPostById(@PathVariable Long id){
        try {
            PostResponse response = postService.findById(id);
            return ApiResponse.<PostResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(response)
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<PostResponse> updatePost(@PathVariable Long id, @ModelAttribute PostRequest request){
        PostResponse response = postService.update(id, request);
        return ApiResponse.<PostResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(response)
                .build();

    }

    @GetMapping("/search/{title}")
    public ApiResponse<List<PostResponse>> getPostsByTitle(@PathVariable String title){
        List<PostResponse> posts = postService.findByTitle(title);
        return ApiResponse.<List<PostResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(posts)
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