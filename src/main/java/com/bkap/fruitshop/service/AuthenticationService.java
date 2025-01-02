package com.bkap.fruitshop.service;

import com.bkap.fruitshop.dto.request.IntrospectRequest;
import com.bkap.fruitshop.dto.request.LoginRequest;
import com.bkap.fruitshop.dto.request.LogoutRequest;
import com.bkap.fruitshop.dto.request.RefreshRequest;
import com.bkap.fruitshop.dto.response.AuthenticationResponse;
import com.bkap.fruitshop.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {

    AuthenticationResponse authenticate(LoginRequest request);
   IntrospectResponse introspect(IntrospectRequest request)throws ParseException, JOSEException;
    void logout(LogoutRequest request) throws ParseException, JOSEException;
    AuthenticationResponse refreshToken(RefreshRequest request) throws JOSEException, ParseException;

}
