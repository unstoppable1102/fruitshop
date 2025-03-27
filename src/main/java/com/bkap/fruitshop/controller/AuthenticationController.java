package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.*;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.AuthenticationResponse;
import com.bkap.fruitshop.dto.response.IntrospectResponse;
import com.bkap.fruitshop.dto.response.UserResponse;
import com.bkap.fruitshop.service.AuthenticationService;
import com.bkap.fruitshop.service.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login (@Valid @RequestBody LoginRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), String.valueOf(errorMessages));
        }
            var result = authenticationService.authenticate(request);
            return ApiResponse.<AuthenticationResponse>builder()
                    .result(result)
                    .build();

    }

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserRequest request, BindingResult result) {

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), String.valueOf(errorMessages));
        }
            return ApiResponse.<UserResponse>builder()
                    .code(HttpStatus.CREATED.value())
                    .message(HttpStatus.CREATED.getReasonPhrase())
                    .result(userService.save(request))
                    .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@Valid @RequestBody IntrospectRequest request, BindingResult result) throws ParseException, JOSEException {
        if (result.hasErrors()) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), result.getFieldError().getDefaultMessage());
        }
        var results = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(results)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@Valid @RequestBody LogoutRequest request, BindingResult result) throws JOSEException, ParseException {
        if (result.hasErrors()) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), result.getFieldError().getDefaultMessage());
        }
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh (@Valid @RequestBody RefreshRequest request, BindingResult result) throws ParseException, JOSEException {
        if (result.hasErrors()) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), result.getFieldError().getDefaultMessage());
        }
        var results = authenticationService.refreshToken(request);
            return ApiResponse.<AuthenticationResponse>builder()
                    .result(results)
                    .build();
    }

}
