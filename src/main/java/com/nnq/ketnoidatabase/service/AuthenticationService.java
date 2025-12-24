package com.nnq.ketnoidatabase.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nnq.ketnoidatabase.dto.request.AuthenticationRequest;
import com.nnq.ketnoidatabase.dto.request.IntrospectRequest;
import com.nnq.ketnoidatabase.dto.request.LogoutRequest;
import com.nnq.ketnoidatabase.dto.request.RefreshTokenRequest;
import com.nnq.ketnoidatabase.dto.response.AuthenticationResponse;
import com.nnq.ketnoidatabase.dto.response.IntrospectResponse;
import com.nnq.ketnoidatabase.entity.InvalidatedToken;
import com.nnq.ketnoidatabase.entity.User;
import com.nnq.ketnoidatabase.exception.AppException;
import com.nnq.ketnoidatabase.exception.ErrorCode;
import com.nnq.ketnoidatabase.repository.InvalidatedTokenRepository;
import com.nnq.ketnoidatabase.repository.UserRepository;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    InvalidatedTokenRepository invalidatedTokenRepository;
    PasswordEncoder passwordEncoder;
    public static final String SINGER_KEY = "vcKsbEnYEQXsnPhertudBG+FySlYEkJ+zdYXPfD5c9Oc6qqDu3TkpJmrgryM6cI5";

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refresh-duration}")
    protected long REFRESH_DURATION;

    public AuthenticationResponse authenticated(AuthenticationRequest request) throws JOSEException {
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOEXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) throw new AppException(ErrorCode.AUTHENTICATED);

        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    public IntrospectResponse introspectResponse(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {

        try {
            var signedJWT = verifyToken(request.getToken(), true);

            String jti = signedJWT.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().Id(jti).expiryTime(expiryTime).build();

            invalidatedTokenRepository.save(invalidatedToken);

        } catch (AppException exception) {
            log.info("Token already expired", exception);
        }
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken(), true);

        var jti = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().Id(jti).expiryTime(expiryTime).build();
        invalidatedTokenRepository.save(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();

        var user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOEXISTED));

        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SINGER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESH_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.AUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.AUTHENTICATED);

        return signedJWT;
    }

    public String generateToken(User user) throws JOSEException {

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("ketnoidatabase.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SINGER_KEY.getBytes())); // ký với thuật toán và chữ ký
            return jwsObject.serialize(); // trả về token
        } catch (JOSEException e) {
            log.error("Không thể tạo token.");
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" "); // nối tất cả các chuỗi với nhau bởi dấu cách
        if (!CollectionUtils.isEmpty(user.getRoles())) // kiểm tra xem role có rỗng hoặc null hay không
        user.getRoles().forEach(role -> {
            stringJoiner.add("ROLE_" + role.getName());
            if (!CollectionUtils.isEmpty(role.getPermissions()))
                role.getPermissions().forEach(permission -> {
                    stringJoiner.add(permission.getName());
                });
        }); // lặp qua từng role và thêm vào stringJoiner
        return stringJoiner.toString(); // trả về stringJoiner
    }
}
