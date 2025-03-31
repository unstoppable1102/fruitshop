package com.bkap.fruitshop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "INVALID MESSAGE KEY", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User already existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1003, "Role not found", HttpStatus.BAD_REQUEST),
    ROLE_EXISTED(1004, "Role already existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User is not exist", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Username or password is incorrect", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_BIRTHDAY(1008, "Invalid Birthday", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(1009, "Product not found", HttpStatus.BAD_REQUEST),
    CART_ITEM_NOT_FOUND(1010, "Cart-item not found", HttpStatus.BAD_REQUEST),
    WISHLIST_NOT_FOUND(1011, "Wishlist not found", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(1012, "Order not found", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1013, "User not found", HttpStatus.BAD_REQUEST),
    POST_CATEGORY_NOT_FOUND(1014, "PostCategory not found", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(1015, "Category not found", HttpStatus.BAD_REQUEST),
    PRODUCT_EXIST_IN_CATEGORY(1016, "product exist in category", HttpStatus.BAD_REQUEST),
    POST_NOT_FOUND(1016, "Post not found", HttpStatus.BAD_REQUEST),
    POST_EXIST_IN_POST_CATEGORY(1016, "post exist in post category", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_FOUND(1017, "Comment not found", HttpStatus.BAD_REQUEST),
    CART_NOT_FOUND(1018, "Cart not found", HttpStatus.BAD_REQUEST),
    PRODUCT_EXISTED(1019, "Product has already existed", HttpStatus.BAD_REQUEST),
    POST_EXISTED(1020, "Post has already existed", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(1021, "Category has already existed", HttpStatus.BAD_REQUEST),
    POST_CATEGORY_EXISTED(1022, "Post Category has already existed", HttpStatus.BAD_REQUEST),
    LOGIN_FAIL(1023, "Username or password is incorrect!", HttpStatus.BAD_REQUEST),
    TOKEN_REQUIRED(1024, "Token must be not blank", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1025, "Token is invalid", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_PRICE(1026, "Product price is invalid", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_STATUS(1027, "Order status is invalid", HttpStatus.BAD_REQUEST),
    ORDER_CANNOT_BE_UPDATED(1028, "Order status can not be updated", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(1029, "Request invalided", HttpStatus.BAD_REQUEST),
    USER_EXIST_IN_ROLE(1030, "User exist in role, cannot be deleted", HttpStatus.BAD_REQUEST),
    CANNOT_DELETE_ADMIN(1031, "User has admin role, cannot be deleted", HttpStatus.BAD_REQUEST),
    INVALID_OLD_PASSWORD(1032, "The password is not correct", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTED(1033, "Email has already existed", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED(1034, "Access denied", HttpStatus.BAD_REQUEST),
    ROLE_INVALID(1035, "Role name is invalid", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1036, "Token is expired", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
