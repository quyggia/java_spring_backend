package com.nnq.ketnoidatabase.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.nnq.ketnoidatabase.dto.request.PermissionCreationRequest;
import com.nnq.ketnoidatabase.dto.response.ApiRespon;
import com.nnq.ketnoidatabase.dto.response.PermissionResponse;
import com.nnq.ketnoidatabase.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionControler {
    PermissionService permissionService;

    @PostMapping
    ApiRespon<PermissionResponse> create(@RequestBody PermissionCreationRequest request) {
        PermissionResponse permissionResponse = permissionService.createPermission(request);
        return ApiRespon.<PermissionResponse>builder()
                .result(permissionResponse)
                .build();
    }

    @GetMapping
    ApiRespon<List<PermissionResponse>> getAllPermisions() {
        var permissions = permissionService.getAllPermissions();
        return ApiRespon.<List<PermissionResponse>>builder().result(permissions).build();
    }

    @DeleteMapping("/{permissionname}")
    ApiRespon<Void> deletePermission(@PathVariable("permissionname") String permissionname) {
        permissionService.deletePermission(permissionname);
        return ApiRespon.<Void>builder().message("Xóa thành công permission.").build();
    }
}
