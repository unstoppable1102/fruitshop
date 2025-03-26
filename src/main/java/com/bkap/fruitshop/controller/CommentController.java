package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.CommentRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.CommentResponse;
import com.bkap.fruitshop.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
    public ApiResponse<CommentResponse> createComment(@Valid @RequestBody CommentRequest request, BindingResult result){
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), String.valueOf(errorMessages));
        }
        return ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .result(commentService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CommentResponse> getCommentById(@PathVariable Long id){
        try {
            return ApiResponse.<CommentResponse>builder()
                    .code(HttpStatus.CREATED.value())
                    .message(HttpStatus.CREATED.getReasonPhrase())
                    .result(commentService.findCommentById(id))
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<CommentResponse> updateComment(@Valid @PathVariable long id, @RequestBody CommentRequest request, BindingResult result){
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), String.valueOf(errorMessages));
        }
            return ApiResponse.<CommentResponse>builder()
                    .code(HttpStatus.CREATED.value())
                    .message(HttpStatus.CREATED.getReasonPhrase())
                    .result(commentService.update(id, request))
                    .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<CommentResponse> deleteCommentById(@PathVariable Long id){
        commentService.delete(id);
        try {
            return ApiResponse.<CommentResponse>builder()
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("Comment is deleted successfully!")
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/posts/{postId}")
    public ApiResponse<List<CommentResponse>> getAllCommentsByUserIdAndPostId(
            @PathVariable long postId,
            @RequestParam(required = false) Long userId) {
        List<CommentResponse> comments;
        if(userId != null) {
            comments = commentService.findAllCommentsByUserIdAndPostId(userId, postId);
        }else {
            comments = commentService.findAllCommentsByPostId(postId);
        }
        try {
            return ApiResponse.<List<CommentResponse>>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(comments)
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
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
