package com.bkap.fruitshop.dto.request;

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

    private String productName;
    private boolean status;
    private double price;
    private double priceOld;
    private int quantity;
    private String description;
    private MultipartFile image;
    private Long categoryId;
}