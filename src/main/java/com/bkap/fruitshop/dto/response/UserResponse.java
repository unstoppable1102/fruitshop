package com.bkap.fruitshop.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class UserResponse {

    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private Date birthday;
    private Set<String> roles;
}
