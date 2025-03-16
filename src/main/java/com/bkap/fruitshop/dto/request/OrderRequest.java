package com.bkap.fruitshop.dto.request;

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
public class OrderRequest {
    private long userId;
    private List<OrderItemRequest> items;
    private String shippingAddress;
    private Date shippingDate;

}
