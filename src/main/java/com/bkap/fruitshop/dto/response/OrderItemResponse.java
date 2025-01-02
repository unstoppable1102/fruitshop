package com.bkap.fruitshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class OrderItemResponse {

    private Long productId;
    private String productName;
    private int quantity;
    private double price;
}
