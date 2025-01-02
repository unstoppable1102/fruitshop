package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.IntrospectRequest;
import com.bkap.fruitshop.dto.request.LoginRequest;
import com.bkap.fruitshop.dto.request.LogoutRequest;
import com.bkap.fruitshop.dto.request.RefreshRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.AuthenticationResponse;
import com.bkap.fruitshop.dto.response.IntrospectResponse;
import com.bkap.fruitshop.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login (@RequestBody LoginRequest request) {
        try {
            var result = authenticationService.authenticate(request);
            return ApiResponse.<AuthenticationResponse>builder()
                    .result(result)
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws JOSEException, ParseException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh (@RequestBody RefreshRequest request) throws ParseException, JOSEException {
            var result = authenticationService.refreshToken(request);
            return ApiResponse.<AuthenticationResponse>builder()
                    .result(result)
                    .build();
    }

}
