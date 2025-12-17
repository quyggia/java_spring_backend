package com.nnq.ketnoidatabase.service;


import com.nnq.ketnoidatabase.dto.request.PermissionCreationRequest;
import com.nnq.ketnoidatabase.dto.response.PermissionResponse;
import com.nnq.ketnoidatabase.entity.Permission;
import com.nnq.ketnoidatabase.mapper.PermissionMapper;
import com.nnq.ketnoidatabase.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepository  permissionRepository;

    PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionCreationRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }
    public List<PermissionResponse> getAllPermissions() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }


    public void deletePermission(String permission) {
        permissionRepository.deleteById(permission);
    }

}
