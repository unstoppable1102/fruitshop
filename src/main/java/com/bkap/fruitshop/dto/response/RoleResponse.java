package com.bkap.fruitshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
    private String name;
    private String description;
    private Set<UserResponse> users;

}
