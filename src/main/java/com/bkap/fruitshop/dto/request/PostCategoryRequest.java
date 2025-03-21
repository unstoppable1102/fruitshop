package com.bkap.fruitshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCategoryRequest {

    @NotBlank(message = "Tên danh mục bài viết không được để trống")
    private String name;
    private boolean active;
    private String description;
}
