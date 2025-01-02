package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.CommentRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.CommentResponse;
import com.bkap.fruitshop.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ApiResponse<List<CommentResponse>> getAllComments() {
        return ApiResponse.<List<CommentResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(commentService.findAllComments())
                .build();
    }

    @PostMapping
    public ApiResponse<CommentResponse> createComment(@RequestBody CommentRequest request){
        return ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .result(commentService.create(request))
                .build();
    }

    @GetMapping("/posts/{postId}")
    public ApiResponse<List<CommentResponse>> getAllCommentsByPostId(@PathVariable long postId) {
        return ApiResponse.<List<CommentResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(commentService.findAllCommentsByPostId(postId))
                .build();
    }

    @GetMapping("/posts/{postId}/count")
    public ApiResponse<Long> countCommentsByPostId(@PathVariable long postId) {
        return ApiResponse.<Long>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(commentService.countCommentsByPostId(postId))
                .build();
    }
}
