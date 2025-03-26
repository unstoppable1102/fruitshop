package com.bkap.fruitshop.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * DTO for {@link com.bkap.fruitshop.entity.Product}
 */
@Setter
@Getter
public class ProductRequest implements Serializable {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String productName;
    private boolean status;
    @PositiveOrZero(message = "Giá sản phẩm không được nhỏ hơn 0")
    private double price;
    @PositiveOrZero(message = "Giá sản phẩm không được nhỏ hơn 0")
    private double priceOld;
    @Min(value = 0, message = "Số lượng sản phẩm không được nhỏ hơn 0")
    private int quantity;
    @NotNull(message = "Mô tả không được để trống")
    private String description;
    private MultipartFile image;
    @NotNull(message = "Danh mục không được để trống")
    private Long categoryId;
}