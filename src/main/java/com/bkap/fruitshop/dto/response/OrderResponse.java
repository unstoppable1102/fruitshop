package com.bkap.fruitshop.dto.response;

import com.bkap.fruitshop.common.util.EOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long orderId;
    private Long userId;
    private EOrderStatus orderStatus;
    private List<OrderItemResponse> items;
    private double total;
    private Date orderDate;
    private String shippingAddress;
    private Date shippingDate;
}

