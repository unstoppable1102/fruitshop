package com.bkap.fruitshop.controller;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<PageResponse<UserResponse>> findAll(
            @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ApiResponse.<PageResponse<UserResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(userService.getAllUsers(pageable))
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
    //@PreAuthorize("hasRole('ADMIN')")
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
    public ApiResponse<UserResponse> updateUserRoles(@PathVariable Long userId, @RequestBody Set<String> roleNames){
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
}
