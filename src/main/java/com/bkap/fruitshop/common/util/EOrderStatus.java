package com.bkap.fruitshop.common.util;

public enum EOrderStatus {

    NEW,        // Đơn hàng mới
    PROCESSING, // Đang xử lý
    SHIPPED,    // Đã giao hàng
    DELIVERED,  // Đã giao thành công
    CANCELED;   // Đã hủy

    public static EOrderStatus fromString(String status) {
        try {
            return EOrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid order status: " + status);
        }
    }
}
