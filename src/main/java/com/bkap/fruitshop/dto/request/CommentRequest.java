package com.bkap.fruitshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest implements Serializable {

    private long parentId; // ID của comment cha (nếu là comment con)

    private long postId; // ID của bài viết mà comment thuộc về

    private long userId; // ID của người dùng tạo comment

    private String content; // Nội dung của comment

}
