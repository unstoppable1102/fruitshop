package com.bkap.fruitshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 20, message = "Tên đăng nhập phải có từ 3 đến 20 ký tự")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 4, message = "Mật khẩu phải ít nhất có 4 ký tự")
    private String password;
}
