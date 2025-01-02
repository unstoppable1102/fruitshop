package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.dto.request.CommentRequest;
import com.bkap.fruitshop.dto.response.CommentResponse;
import com.bkap.fruitshop.entity.Comment;
import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;
import com.bkap.fruitshop.repository.CommentRepository;
import com.bkap.fruitshop.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CommentResponse> findAllComments() {
        return commentRepository.findAll().stream()
                .map((element) -> modelMapper.map(element, CommentResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse findCommentById(long id) {
        Comment comment =  commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        return modelMapper.map(comment, CommentResponse.class);
    }

    @Override
    public CommentResponse create(CommentRequest request) {
        Comment comment = modelMapper.map(request, Comment.class);
        Comment savedComment = commentRepository.save(comment);
        return modelMapper.map(savedComment, CommentResponse.class);
    }

    @Override
    public CommentResponse update(long id, CommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        modelMapper.map(request, comment);
        return modelMapper.map(commentRepository.save(comment), CommentResponse.class);
    }

    @Override
    public void delete(long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentResponse> findAllCommentsByPostId(long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map((element) -> modelMapper.map(element, CommentResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public long countCommentsByPostId(long postId) {
        return commentRepository.countByPostId(postId);
    }
}
