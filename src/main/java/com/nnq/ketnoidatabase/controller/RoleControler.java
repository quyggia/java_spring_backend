package com.nnq.ketnoidatabase.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.nnq.ketnoidatabase.dto.request.RoleCreationRequest;
import com.nnq.ketnoidatabase.dto.response.ApiRespon;
import com.nnq.ketnoidatabase.dto.response.RoleResponse;
import com.nnq.ketnoidatabase.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/role")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleControler {

    RoleService roleService;

    @PostMapping
    ApiRespon<RoleResponse> create(@RequestBody RoleCreationRequest request) {
        RoleResponse role = roleService.createRole(request);
        return ApiRespon.<RoleResponse>builder().result(role).build();
    }

    @GetMapping
    ApiRespon<List<RoleResponse>> getAllRole() {
        return ApiRespon.<List<RoleResponse>>builder()
                .result(roleService.getAllRole())
                .build();
    }

    @DeleteMapping("/{role}")
    ApiRespon<Void> deleteRole(@PathVariable("role") String role) {
        roleService.deleteRole(role);
        return ApiRespon.<Void>builder().message("Xóa thành công role.").build();
    }
}
