package com.bkap.fruitshop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T result;

    // Factory method for error case.
    public static <T> ApiResponse<T> errorResponse(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}