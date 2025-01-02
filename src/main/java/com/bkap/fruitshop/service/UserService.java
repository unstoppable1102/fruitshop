package com.bkap.fruitshop.service;

import com.bkap.fruitshop.dto.request.UserRequest;
import com.bkap.fruitshop.dto.response.UserResponse;
import com.bkap.fruitshop.entity.User;

import java.util.List;

public interface UserService {

     UserResponse save(UserRequest userRequest);
     List<UserResponse> getAllUsers();
     UserResponse getUserById(Long id);
     UserResponse getMyInfo();
     UserResponse updateUser(Long id, UserRequest userRequest);
     void deleteUser(Long id);
     User findByUsername(String username);

}
