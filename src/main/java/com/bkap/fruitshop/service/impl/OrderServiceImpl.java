package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.common.enums.EOrderStatus;
import com.bkap.fruitshop.dto.request.OrderItemRequest;
import com.bkap.fruitshop.dto.request.OrderRequest;
import com.bkap.fruitshop.dto.response.OrderResponse;
import com.bkap.fruitshop.entity.Order;
import com.bkap.fruitshop.entity.OrderItem;
import com.bkap.fruitshop.entity.Product;
import com.bkap.fruitshop.entity.User;
import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;
import com.bkap.fruitshop.repository.OrderRepository;
import com.bkap.fruitshop.repository.ProductRepository;
import com.bkap.fruitshop.repository.UserRepository;
import com.bkap.fruitshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->new AppException(ErrorCode.USER_NOT_FOUND));


        Map<Long, Product> productMap = productRepository.findAllById(
                request.getItems().stream()
                        .map(OrderItemRequest::getProductId)
                        .toList()
        ).stream().collect(Collectors.toMap(Product::getId, p -> p));

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for(OrderItemRequest itemRequest : request.getItems()) {
            Product product = productMap.get(itemRequest.getProductId());

            if(product == null) throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
            if (product.getPrice() == 0) throw new AppException(ErrorCode.INVALID_PRODUCT_PRICE);

            double itemPrice = product.getPrice() * itemRequest.getQuantity();
            totalPrice += itemPrice;

            orderItems.add(OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .price(itemPrice)
                    .build());
        }
        double finalTotalPrice = totalPrice;
        Order order = Order.builder()
                .user(user)
                .orderStatus(EOrderStatus.NEW)
                .shippingAddress(request.getShippingAddress())
                .shippingDate(request.getShippingDate())
                .total(finalTotalPrice)
                .orderItems(orderItems)
                .build();

        // Gán lại order vào từng orderItem sau khi build
        orderItems.forEach(item -> item.setOrder(order));

        return modelMapper.map(orderRepository.save(order), OrderResponse.class);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(orderResponse -> modelMapper.map(orders, OrderResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        var orderStatus = EOrderStatus.fromString(status);
        if (!isValidStatusTransaction(order.getOrderStatus(), orderStatus)){
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }
        order.setOrderStatus(orderStatus);
        return modelMapper.map(orderRepository.save(order), OrderResponse.class);
    }

    private boolean isValidStatusTransaction(EOrderStatus current, EOrderStatus next) {
        return switch (current){
            case NEW -> next == EOrderStatus.PROCESSING;
            case PROCESSING -> next == EOrderStatus.SHIPPING || next == EOrderStatus.CANCELED;
            case SHIPPING -> next == EOrderStatus.DELIVERED || next == EOrderStatus.CANCELED;
            case DELIVERED, CANCELED -> false;
        };
    }
}
