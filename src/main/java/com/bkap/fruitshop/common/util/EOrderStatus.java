package com.bkap.fruitshop.common.util;

import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;

public enum EOrderStatus {

    NEW,        // Đơn hàng mới
    PROCESSING, // Đang xử lý
    SHIPPING,    // Đang giao hàng
    DELIVERED,  // Đã giao thành công
    CANCELED;   // Đã hủy

    public static EOrderStatus fromString(String status) {
        if (status == null || status.isBlank()) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }
        try {
            return EOrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid order status: " + status);
        }
    }
}
