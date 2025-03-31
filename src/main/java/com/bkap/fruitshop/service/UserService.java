package com.bkap.fruitshop.service;

import com.bkap.fruitshop.dto.request.ResetPasswordRequest;
import com.bkap.fruitshop.dto.request.RoleNameRequest;
import com.bkap.fruitshop.dto.request.UserRequest;
import com.bkap.fruitshop.dto.request.UserUpdateInforRequest;
import com.bkap.fruitshop.dto.response.PageResponse;
import com.bkap.fruitshop.dto.response.UserResponse;
import com.bkap.fruitshop.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface UserService {

     UserResponse save(UserRequest userRequest);
     PageResponse<UserResponse> getAllUsers(String keyword, Pageable pageable);
     UserResponse getUserById(Long id);
     UserResponse getMyInfo();
     UserResponse updateUser(Long id, UserUpdateInforRequest request);
     void deleteUser(Long id);
     User findByUsername(String username);
     UserResponse updateUserRoles(Long userId, RoleNameRequest roleNames);
     void forgotPassword(String email);
     void resetPassword(ResetPasswordRequest request);
     void changePassword(Long userId, String oldPassword, String newPassword);
     String findUsernameById(Long id);
     UserResponse toggleUserStatus(Long userId, boolean enabled);
     UserResponse updateUserAvatar(Long userId, MultipartFile avatarFile);
     long countUsers();
     boolean verifyUserAccount(String token);
}
