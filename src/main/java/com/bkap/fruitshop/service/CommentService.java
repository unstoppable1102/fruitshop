package com.bkap.fruitshop.service;

import com.bkap.fruitshop.dto.request.CommentRequest;
import com.bkap.fruitshop.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {

    List<CommentResponse> findAllComments();
    CommentResponse findCommentById(long id);
    CommentResponse create(CommentRequest request);
    CommentResponse update(long id, CommentRequest request);
    void delete(long id);
    List<CommentResponse> findAllCommentsByPostId(long postId);
    List<CommentResponse> findAllCommentsByUserIdAndPostId(Long userId, long postId);
    long countCommentsByPostId(long postId);
    CommentResponse approveComment(long id, boolean isApproved);
}
