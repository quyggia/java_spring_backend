package com.nnq.ketnoidatabase.controler;

import com.nnq.ketnoidatabase.dto.response.ApiRespon;
import com.nnq.ketnoidatabase.dto.request.UserCreationRequest;
import com.nnq.ketnoidatabase.dto.request.UserUpdateRequest;
import com.nnq.ketnoidatabase.dto.response.UserResponse;
import com.nnq.ketnoidatabase.entity.User;
import com.nnq.ketnoidatabase.service.Userservice;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserControler {

    Userservice userservice;

    @PostMapping
    ApiRespon<User> createUser(@RequestBody @Valid UserCreationRequest request) {

        ApiRespon<User> apiRespon = new ApiRespon<>();
        apiRespon.setResult(userservice.createUser(request));
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
    UserResponse getMyUser() {//lấy tham soso sừ url gán cho biến userId
        return userservice.getMyUser();
    }
    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable("userId") String userId) {//lấy tham soso sừ url gán cho biến userId
        return userservice.getUser(userId);
    }

    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable("userId") String id , @RequestBody UserUpdateRequest request)
    {
        return userservice.updateUser(id, request);
    }
    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable("userId") String userId) {
        userservice.deleteUser(userId);
        return "Xóa thành công.";
    }
}
