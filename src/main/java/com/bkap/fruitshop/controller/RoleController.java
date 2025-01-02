package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.RoleRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.RoleResponse;
import com.bkap.fruitshop.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    public ApiResponse<String> deleteRole(@PathVariable String role) {
        roleService.delete(role);
        return ApiResponse.<String>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Role is deleted successfully!")
                .build();

    }
}
