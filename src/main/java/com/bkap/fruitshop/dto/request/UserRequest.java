package com.bkap.fruitshop.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserRequest {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 20, message = "Tên đăng nhập phải có từ 3 đến 20 ký tự")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 4, message = "Mật khẩu phải ít nhất có 4 ký tự")
    private String password;

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10,11}$", message = "Số điện thoại phải có 10 hoặc 11 chữ số")
    private String phone;

    @NotBlank(message = "Giới tính không được để trống")
    private boolean gender;

    @Past(message = "Ngày sinh không phải là ngày trong quá khứ")
    private Date birthday;
}
