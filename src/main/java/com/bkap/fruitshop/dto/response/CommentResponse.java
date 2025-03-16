package com.bkap.fruitshop.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Builder
public class CommentResponse {
    private Long id; // ID của comment

    private Long parentCommentId; // ID của comment cha (nếu có)

    private long postId; // ID của bài viết mà comment thuộc về

    private String content; // Nội dung của comment

    private String username; // Tên người dùng đã tạo comment

    private String postTitle;

}
