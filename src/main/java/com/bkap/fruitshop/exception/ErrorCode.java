package com.bkap.fruitshop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    // System
    UNCATEGORIZED_EXCEPTION(9999, "error.uncategorized",                HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY            (1001, "error.invalidKey",                   HttpStatus.BAD_REQUEST),
    INVALID_REQUEST        (1029, "error.request.invalid",              HttpStatus.BAD_REQUEST),
    ACCESS_DENIED          (1034, "error.access.denied",                HttpStatus.FORBIDDEN),

    // Auth
    UNAUTHENTICATED        (1006, "error.unauthenticated",              HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED           (1007, "error.unauthorized",                 HttpStatus.FORBIDDEN),
    INVALID_TOKEN          (1025, "error.token.invalid",                HttpStatus.UNAUTHORIZED),
    TOKEN_REQUIRED         (1024, "error.token.required",               HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED          (1036, "error.token.expired",                HttpStatus.UNAUTHORIZED),

    // User
    USER_NOT_FOUND         (1005, "error.user.notFound",                HttpStatus.NOT_FOUND),
    USER_EXISTED           (1002, "error.user.existed",                 HttpStatus.BAD_REQUEST),
    USER_EXIST_IN_ROLE     (1030, "error.user.existInRole",             HttpStatus.BAD_REQUEST),
    CANNOT_DELETE_ADMIN    (1031, "error.user.cannotDeleteAdmin",       HttpStatus.BAD_REQUEST),
    INVALID_OLD_PASSWORD   (1032, "error.password.invalid",             HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTED  (1033, "error.email.existed",                HttpStatus.BAD_REQUEST),
    INVALID_BIRTHDAY       (1008, "error.birthday.invalid",             HttpStatus.BAD_REQUEST),

    // Role
    ROLE_NOT_FOUND         (1003, "error.role.notFound",                HttpStatus.NOT_FOUND),
    ROLE_EXISTED           (1004, "error.role.existed",                 HttpStatus.BAD_REQUEST),
    ROLE_INVALID           (1035, "error.role.invalid",                 HttpStatus.BAD_REQUEST),

    // Product
    PRODUCT_NOT_FOUND      (1009, "error.product.notFound",             HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED        (1019, "error.product.existed",              HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_PRICE  (1026, "error.product.invalidPrice",         HttpStatus.BAD_REQUEST),
    PRODUCT_EXIST_IN_CATEGORY(1016, "error.category.productExist",      HttpStatus.BAD_REQUEST),

    // Category
    CATEGORY_NOT_FOUND     (1015, "error.category.notFound",            HttpStatus.NOT_FOUND),
    CATEGORY_EXISTED       (1021, "error.category.existed",             HttpStatus.BAD_REQUEST),

    // Order
    ORDER_NOT_FOUND        (1012, "error.order.notFound",               HttpStatus.NOT_FOUND),
    INVALID_ORDER_STATUS   (1027, "error.order.invalidStatus",          HttpStatus.BAD_REQUEST),
    ORDER_CANNOT_BE_UPDATED(1028, "error.order.cannotUpdate",           HttpStatus.BAD_REQUEST),

    // Cart
    CART_NOT_FOUND         (1018, "error.cart.notFound",                HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND    (1010, "error.cartItem.notFound",            HttpStatus.NOT_FOUND),
    INVALID_QUANTITY       (1037, "error.quantity.invalid",             HttpStatus.BAD_REQUEST),

    // Wishlist
    WISHLIST_NOT_FOUND     (1011, "error.wishlist.notFound",            HttpStatus.NOT_FOUND),

    // Post
    POST_NOT_FOUND         (1038, "error.post.notFound",                HttpStatus.NOT_FOUND),
    POST_EXISTED           (1020, "error.post.existed",                 HttpStatus.BAD_REQUEST),
    POST_EXIST_IN_POST_CATEGORY(1039, "error.post.existInCategory",     HttpStatus.BAD_REQUEST),

    // Post Category
    POST_CATEGORY_NOT_FOUND(1014, "error.postCategory.notFound",        HttpStatus.NOT_FOUND),
    POST_CATEGORY_EXISTED  (1022, "error.postCategory.existed",         HttpStatus.BAD_REQUEST),

    // Comment
    COMMENT_NOT_FOUND      (1017, "error.comment.notFound",             HttpStatus.NOT_FOUND),
    ;

    ErrorCode(int code, String messageKey, HttpStatusCode statusCode) {
        this.code = code;
        this.messageKey = messageKey;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String messageKey;
    private final HttpStatusCode statusCode;
}
