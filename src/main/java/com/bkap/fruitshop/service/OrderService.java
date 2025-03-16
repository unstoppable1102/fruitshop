package com.bkap.fruitshop.service;

import com.bkap.fruitshop.dto.request.OrderRequest;
import com.bkap.fruitshop.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest orderRequest);
    List<OrderResponse> getOrdersByUserId(Long userId);
    OrderResponse getOrderById(Long orderId);
    OrderResponse updateOrderStatus(Long orderId, String status);
}
