package com.bkap.fruitshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private String status;
    private List<OrderItemResponse> items;
    private LocalDateTime orderDate;
}
