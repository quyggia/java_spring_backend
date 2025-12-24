package com.nnq.ketnoidatabase.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nnq.ketnoidatabase.dto.request.UserCreationRequest;
import com.nnq.ketnoidatabase.dto.request.UserUpdateRequest;
import com.nnq.ketnoidatabase.dto.response.UserResponse;
import com.nnq.ketnoidatabase.entity.User;
import com.nnq.ketnoidatabase.exception.AppException;
import com.nnq.ketnoidatabase.exception.ErrorCode;
import com.nnq.ketnoidatabase.mapper.UserMapper;
import com.nnq.ketnoidatabase.repository.RoleRepository;
import com.nnq.ketnoidatabase.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class Userservice {

    UserRepository userRepository;
    UserMapper userMapper;

    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public User createUser(UserCreationRequest request) {

        if (userRepository.existsUserByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        com.nnq.ketnoidatabase.entity.Role userRole = roleRepository
                .findById(com.nnq.ketnoidatabase.enums.Role.USER.name())
                .orElseThrow(() -> new RuntimeException("Role USER not found"));

        Set<com.nnq.ketnoidatabase.entity.Role> roles = new HashSet<>();
        roles.add(userRole);

        user.setRoles(roles);

        return userRepository.save(user);
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay"));
        userMapper.updateMapper(request, user);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles()); // chus ys

        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userid) {
        if (!userRepository.existsUserById(userid)) throw new RuntimeException("User muốn xóa không tồn tại.");
        userRepository.deleteById(userid);
    }

    @PreAuthorize("hasRole('ADMIN')")
    // hasAnyAuthority("ROLE_ADMIN")
    public List<UserResponse> getUser() {
        log.info("Đã vào được methor getuser");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
        // List<User> → Stream<User> → UserResponse → List<UserResponse>
    }

    @PostAuthorize("returnObject.username == authentication.name") // Chỉ cho phép xem chính mình (admin != nnq01)
    public UserResponse getUser(String id) {
        log.info("Đã vào được methor getuser by id");
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay")));
    }

    public UserResponse getMyUser() {
        var name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.LOI_CHUA_XACDINH));
        return userMapper.toUserResponse(user);
    }
}
