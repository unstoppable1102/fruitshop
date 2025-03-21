package com.bkap.fruitshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.bkap.fruitshop.entity.Category}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest implements Serializable {
    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;
    private boolean status;
}