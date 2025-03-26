package com.bkap.fruitshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequest {
    @NotBlank(message = "productId is required")
    private long productId;

    @NotBlank(message = "quantity is required")
    @PositiveOrZero(message = "quantity must be great than 0")
    private int quantity;
}
