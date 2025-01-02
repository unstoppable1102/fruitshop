package com.bkap.fruitshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String productName;
    private boolean status;
    private double price;
    private int quantity;
    private String description;
    private String image;
    private String categoryName; // Để hiển thị tên danh mục nếu cần

}
