package com.nnq.ketnoidatabase.controler;


import com.nimbusds.jose.JOSEException;
import com.nnq.ketnoidatabase.dto.request.IntrospectRequest;
import com.nnq.ketnoidatabase.dto.request.LogoutRequest;
import com.nnq.ketnoidatabase.dto.request.RefreshTokenRequest;
import com.nnq.ketnoidatabase.dto.response.ApiRespon;
import com.nnq.ketnoidatabase.dto.request.AuthenticationRequest;
import com.nnq.ketnoidatabase.dto.response.AuthenticationResponse;
import com.nnq.ketnoidatabase.dto.response.IntrospectResponse;
import com.nnq.ketnoidatabase.service.AuthenticationService;
import lombok.AccessLevel;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationControler {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiRespon<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws JOSEException {
        var result = authenticationService.authenticated(request);
//        return ApiRespon.<AuthenticationResponse>builder()
//                .result(AuthenticationResponse.builder()
//                        .authenticated(result)
//                        .build())
//                .build();
//        ApiRespon<AuthenticationResponse>  apiRespon = new ApiRespon<>();
//        apiRespon.setResult(AuthenticationResponse.builder()
//                        .token(result.getToken())
//                        .authenticated(true)
//                    .build());
        return ApiRespon.<AuthenticationResponse>builder()
                        .result(result)
                        .build();
    }
    @PostMapping("/introspect")
    ApiRespon<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
        var result = authenticationService.introspectResponse(request);
        return ApiRespon.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/refresh")
    ApiRespon<AuthenticationResponse> introspect(@RequestBody RefreshTokenRequest request) throws JOSEException, ParseException {
        var result = authenticationService.refreshToken(request);
        return ApiRespon.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
    @PostMapping("/logout")
    ApiRespon<Void> logout(@RequestBody LogoutRequest request) throws JOSEException, ParseException {
        authenticationService.logout(request);
        return ApiRespon.<Void>builder()
                .build();
    }
}
