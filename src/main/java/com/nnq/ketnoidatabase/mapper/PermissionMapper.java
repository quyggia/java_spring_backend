package com.nnq.ketnoidatabase.mapper;

import org.mapstruct.Mapper;

import com.nnq.ketnoidatabase.dto.request.PermissionCreationRequest;
import com.nnq.ketnoidatabase.dto.response.PermissionResponse;
import com.nnq.ketnoidatabase.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionResponse toPermissionResponse(Permission permission);

    Permission toPermission(PermissionCreationRequest request);
}
