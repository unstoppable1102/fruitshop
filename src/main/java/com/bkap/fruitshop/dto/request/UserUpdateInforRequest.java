package com.bkap.fruitshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserUpdateInforRequest {

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    private String email;
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10,11}$", message = "Số điện thoại phải có 10 hoặc 11 chữ số")
    private String phone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @Past(message = "Ngày sinh không phải là ngày trong quá khứ")
    private Date birthday;
}
