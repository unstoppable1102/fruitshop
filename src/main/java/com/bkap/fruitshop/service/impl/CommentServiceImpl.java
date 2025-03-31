package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.dto.request.CommentRequest;
import com.bkap.fruitshop.dto.response.CommentResponse;
import com.bkap.fruitshop.entity.Comment;
import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;
import com.bkap.fruitshop.repository.CommentRepository;
import com.bkap.fruitshop.repository.PostRepository;
import com.bkap.fruitshop.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public List<CommentResponse> findAllComments() {
        return commentRepository.findAll().stream()
                .map(comment -> {
                    CommentResponse response = modelMapper.map(comment, CommentResponse.class);
                    response.setUsername(comment.getUser().getUsername());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse findCommentById(long id) {
        Comment comment =  commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        return mapToResponse(comment);
    }

    @Override
    public CommentResponse create(CommentRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(request.getContent());

        if (request.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
            comment.setParentComment(parentComment);
        }
        comment = commentRepository.save(comment);
        // Tạo response
        return mapToResponse(comment);
    }

    @Override
    public CommentResponse update(long id, CommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        comment.setContent(request.getContent());
        comment = commentRepository.save(comment);

        return mapToResponse(comment);
    }

    @Override
    public void delete(long id) {
        if (!commentRepository.existsById(id)) {
            throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
        }
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentResponse> findAllCommentsByPostId(long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map((element) -> modelMapper.map(element, CommentResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> findAllCommentsByUserIdAndPostId(Long userId, long postId) {
        return commentRepository.findByUserIdAndPostId(userId, postId).stream()
                .map((element) -> modelMapper.map(element, CommentResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public long countCommentsByPostId(long postId) {
        return commentRepository.countByPostId(postId);
    }

    @Override
    public CommentResponse approveComment(long id, boolean isApproved) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        comment.setApproved(isApproved);
        commentRepository.save(comment);
        return mapToResponse(comment);
    }

    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .username(comment.getUser().getUsername()) // Đảm bảo ánh xạ đúng
                .postTitle(comment.getPost().getTitle())
                .build();
    }

}
