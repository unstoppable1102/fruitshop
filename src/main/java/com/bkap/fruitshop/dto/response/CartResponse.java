package com.bkap.fruitshop.dto.response;
import jakarta.persistence.Transient;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {

    private Long id;
    private Long userId;
    private List<CartItemResponse> items;
    private double totalPrice;
    @Transient
    private int countCartItem;

}
