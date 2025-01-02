package com.bkap.fruitshop.repository;

import com.bkap.fruitshop.dto.response.OrderResponse;
import com.bkap.fruitshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
   List<OrderResponse> findByUserId(long userId);
}