package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.response.OrderResponse;
import com.bkap.fruitshop.service.OrderService;
import com.bkap.fruitshop.dto.request.OrderRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ApiResponse<List<OrderResponse>> getUserOrders(@RequestParam long userId) {
        return ApiResponse.<List<OrderResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(orderService.getOrdersByUserId(userId))
                .build();
    }

    @PostMapping
    public ApiResponse<OrderResponse> createUserOrder(@RequestParam long userId, @RequestBody OrderRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .result(orderService.createOrder(userId, request))
                .build();
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getUserOrder(@PathVariable long orderId) {
        return ApiResponse.<OrderResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(orderService.getOrderById(orderId))
                .build();
    }

    @PatchMapping("/{orderId}/status")
    public ApiResponse<OrderResponse> updateUserOrder(@PathVariable long orderId, @RequestParam String status) {
        return ApiResponse.<OrderResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(orderService.updateOrderStatus(orderId, status))
                .build();
    }
}
