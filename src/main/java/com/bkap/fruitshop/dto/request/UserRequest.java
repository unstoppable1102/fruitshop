package com.bkap.fruitshop.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserRequest {
    private String username;

    private String password;

    private String fullName;

    private String email;

    private String phone;

    private Date birthday;
}
