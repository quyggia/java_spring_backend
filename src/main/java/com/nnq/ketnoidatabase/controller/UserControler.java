package com.nnq.ketnoidatabase.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.nnq.ketnoidatabase.dto.request.UserCreationRequest;
import com.nnq.ketnoidatabase.dto.request.UserUpdateRequest;
import com.nnq.ketnoidatabase.dto.response.ApiRespon;
import com.nnq.ketnoidatabase.dto.response.UserResponse;
import com.nnq.ketnoidatabase.entity.User;
import com.nnq.ketnoidatabase.mapper.UserMapper;
import com.nnq.ketnoidatabase.service.Userservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserControler {

    Userservice userservice;

    UserMapper userMapper;

    @PostMapping
    ApiRespon<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {

        User user = userservice.createUser(request);
        UserResponse userResponse = userMapper.toUserResponse(user);

        ApiRespon<UserResponse> apiRespon = new ApiRespon<>();
        apiRespon.setResult(userResponse);
        apiRespon.setCode(1000);
        return apiRespon;
    }

    @GetMapping
    ApiRespon<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("UserName : {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiRespon.<List<UserResponse>>builder()
                .result(userservice.getUser())
                .build();
    }

    @GetMapping("/myinfo")
    UserResponse getMyUser() { // lấy tham soso sừ url gán cho biến userId
        return userservice.getMyUser();
    }

    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable("userId") String userId) { // lấy tham soso sừ url gán cho biến userId
        return userservice.getUser(userId);
    }

    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable("userId") String id, @RequestBody UserUpdateRequest request) {
        return userservice.updateUser(id, request);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable("userId") String userId) {
        userservice.deleteUser(userId);
        return "Xóa thành công.";
    }
}
