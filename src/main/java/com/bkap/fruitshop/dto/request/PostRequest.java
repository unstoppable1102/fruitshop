package com.bkap.fruitshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest implements Serializable {
    @NotBlank(message = "Tên bài viết không được để trống")
    @Size(min = 5, message = "Tên bài viết phải từ 5 ký tự")
    private  String title;

    @NotBlank(message = "Mô tả ngắn không được để trống")
    private  String shortDescription;

    @NotBlank(message = "Nội dung không được để trống")
    private  String content;
    private MultipartFile image;

    @NotBlank(message = "Danh mục bài viết không đc để trống")
    private long postCategoryId;
    private boolean status;
}
