package com.bkap.fruitshop.mapper;

import com.bkap.fruitshop.dto.response.UserResponse;
import com.bkap.fruitshop.entity.Role;
import com.bkap.fruitshop.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserResponse toResponse(User user) {

        UserResponse userResponse = modelMapper.map(user, UserResponse.class);

        userResponse.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return userResponse;
    }
}
