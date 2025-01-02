package com.bkap.fruitshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class CommentResponse {
    private Long id; // ID của comment

    private Long parentId; // ID của comment cha (nếu có)

    private Long postId; // ID của bài viết mà comment thuộc về

    private String content; // Nội dung của comment

    private String username; // Tên người dùng đã tạo comment

    private String postTitle;

}
