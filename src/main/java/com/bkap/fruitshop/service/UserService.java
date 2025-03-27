package com.bkap.fruitshop.service;

import com.bkap.fruitshop.dto.request.UserRequest;
import com.bkap.fruitshop.dto.request.UserUpdateInforRequest;
import com.bkap.fruitshop.dto.response.PageResponse;
import com.bkap.fruitshop.dto.response.UserResponse;
import com.bkap.fruitshop.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface UserService {

     UserResponse save(UserRequest userRequest);
     PageResponse<UserResponse> getAllUsers(Pageable pageable);
     UserResponse getUserById(Long id);
     UserResponse getMyInfo();
     UserResponse updateUser(Long id, UserUpdateInforRequest request);
     void deleteUser(Long id);
     User findByUsername(String username);
     UserResponse updateUserRoles(Long userId, Set<String> roleNames);
     void forgotPassword(String email);
     void resetPassword(String token, String newPassword);
     void changePassword(Long userId, String oldPassword, String newPassword);
     String findUsernameById(Long id);

}
