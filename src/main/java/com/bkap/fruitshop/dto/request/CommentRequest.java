package com.bkap.fruitshop.dto.request;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Mã bài viết không đc để trống")
    private long postId; // ID của bài viết mà comment thuộc về

    @NotBlank(message = "Mã người dùng không được để trống")
    private Long userId; // ID của người dùng tạo comment

    private Long parentCommentId; // ID của comment cha (nếu là comment con)

    @NotBlank(message = "Nội dung comment không đc để trống")
    private String content; // Nội dung của comment

}
