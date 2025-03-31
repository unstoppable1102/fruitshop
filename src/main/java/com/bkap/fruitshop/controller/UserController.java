package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.ResetPasswordRequest;
import com.bkap.fruitshop.dto.request.RoleNameRequest;
import com.bkap.fruitshop.dto.request.UserRequest;
import com.bkap.fruitshop.dto.request.UserUpdateInforRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.PageResponse;
import com.bkap.fruitshop.dto.response.UserResponse;
import com.bkap.fruitshop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<PageResponse<UserResponse>> findAll(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ApiResponse.<PageResponse<UserResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(userService.getAllUsers(keyword, pageable))
                .build();
    }

    @PostMapping
    public ApiResponse<UserResponse> save(@RequestBody UserRequest request){
        try {
            return ApiResponse.<UserResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(userService.save(request))
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ApiResponse<UserResponse> findById(@PathVariable Long id){
        try {
            return ApiResponse.<UserResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(userService.getUserById(id))
                    .build();
        }catch (Exception e){
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/getMyInfo")
    public ApiResponse<UserResponse> getMyInfo(){
        try {
            return ApiResponse.<UserResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(userService.getMyInfo())
                    .build();
        }catch (Exception e){
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> update(@Valid @PathVariable Long id, @RequestBody UserUpdateInforRequest request, BindingResult result){
        if(result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), String.valueOf(errorMessages));
        }
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(userService.updateUser(id, request))
                .build();
    }

    @PutMapping("/{userId}/roles")
    public ApiResponse<UserResponse> updateUserRoles(@PathVariable Long userId, @RequestBody RoleNameRequest roleNames){
        try {
            return ApiResponse.<UserResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(userService.updateUserRoles(userId, roleNames))
                    .build();
        }catch (Exception e){
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<UserResponse> delete(@PathVariable Long id){
        try {
            userService.deleteUser(id);
            return ApiResponse.<UserResponse>builder()
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("User is deleted successfully!")
                    .build();
        }catch (Exception e){
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PatchMapping("/change-password")
    public ApiResponse<String> changePassword(@RequestParam Long userId,
                                              @RequestParam String oldPassword,
                                              @RequestParam String newPassword){
        try {
            userService.changePassword(userId, oldPassword, newPassword);
            return ApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("Password changed successfully!")
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@RequestParam String email){
        userService.forgotPassword(email);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Password forgot successfully!")
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody ResetPasswordRequest request){
        userService.resetPassword(request);

        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Password reset successfully!")
                .build();
    }

    @PatchMapping("/{userId}/toggle-status")
    public ApiResponse<UserResponse> toggleStatus(@PathVariable Long userId, @RequestParam boolean status){
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(userService.toggleUserStatus(userId, status))
                .build();
    }

    @PostMapping("/{id}/avatar")
    public ApiResponse<UserResponse> updateUserAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar){
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(userService.updateUserAvatar(id, avatar))
                .build();
    }

    @GetMapping("/count")
    public ApiResponse<Long> countUsers(){
        return ApiResponse.<Long>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(userService.countUsers())
                .build();
    }

    @GetMapping("/verify")
    public ApiResponse<Boolean> verifyUser(@RequestParam String token){
        return ApiResponse.<Boolean>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                 .result(userService.verifyUserAccount(token))
                .build();
    }
}
