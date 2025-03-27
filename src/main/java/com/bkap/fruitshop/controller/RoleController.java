package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.RoleRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.RoleResponse;
import com.bkap.fruitshop.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@Valid @RequestBody RoleRequest request, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), String.valueOf(errorMessages));
        }
        return ApiResponse.<RoleResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        try {
            return ApiResponse.<List<RoleResponse>>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(roleService.getAll())
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @DeleteMapping("/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteRole(@PathVariable String role) {
        try {
            roleService.delete(role);
            return ApiResponse.<String>builder()
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("Role is deleted successfully!")
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }
}
