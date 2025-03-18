package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.common.util.EOrderStatus;
import com.bkap.fruitshop.dto.request.OrderItemRequest;
import com.bkap.fruitshop.dto.request.OrderRequest;
import com.bkap.fruitshop.dto.response.OrderItemResponse;
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

        Order order = new Order();
        order.setUser(user);
        System.out.println("User ID trong Order: " + order.getUser().getId());
        order.setOrderStatus(EOrderStatus.NEW);

        List<Long> productIds = request.getItems().stream()
                .map(OrderItemRequest::getProductId)
                .toList();

        Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for(OrderItemRequest itemRequest : request.getItems()) {
            Product product = productMap.get(itemRequest.getProductId());
            if(product == null) {
                throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
            }
            if (product.getPrice() == 0) {
                throw new AppException(ErrorCode.INVALID_PRODUCT_PRICE);
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice() * orderItem.getQuantity());
            totalPrice += orderItem.getPrice();
            orderItems.add(orderItem);
        }
        order.setTotal(totalPrice);
        order.setShippingAddress(request.getShippingAddress());
        order.setShippingDate(request.getShippingDate());
        order.setOrderItems(orderItems);
        return modelMapper.map(orderRepository.save(order), OrderResponse.class);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        List<OrderResponse> orderResponses = orderRepository.findByUserId(userId);
        return orderResponses.stream()
                .map(orderResponse -> modelMapper.map(orderResponse, OrderResponse.class))
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
