package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.CommentRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.CommentResponse;
import com.bkap.fruitshop.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CommentResponse> createComment(@Valid @RequestBody CommentRequest request){

        return ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .result(commentService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CommentResponse> getCommentById(@PathVariable Long id){

        return ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .result(commentService.findCommentById(id))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CommentResponse> updateComment(@Valid @PathVariable long id, @RequestBody CommentRequest request){

        return ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .result(commentService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<CommentResponse> deleteCommentById(@PathVariable Long id){

        commentService.delete(id);
        return ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Comment is deleted successfully!")
                .build();
    }

    @GetMapping("/posts/{postId}")
    public ApiResponse<List<CommentResponse>> getCommentsByUserIdAndPostId(
            @PathVariable long postId,
            @RequestParam(required = false) Long userId) {
        List<CommentResponse> comments = (userId != null)
        ? commentService.findAllCommentsByUserIdAndPostId(userId, postId)
        : commentService.findAllCommentsByPostId(postId);

        return ApiResponse.<List<CommentResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(comments)
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

    @PatchMapping("/{commentId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CommentResponse> approveComment(@PathVariable Long commentId, @RequestParam boolean isApproved) {

        return ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(commentService.approveComment(commentId, isApproved))
                .build();
    }
}
