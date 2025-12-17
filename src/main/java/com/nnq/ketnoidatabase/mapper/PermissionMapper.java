package com.nnq.ketnoidatabase.mapper;

import com.nnq.ketnoidatabase.dto.request.PermissionCreationRequest;
import com.nnq.ketnoidatabase.dto.response.PermissionResponse;
import com.nnq.ketnoidatabase.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionResponse toPermissionResponse(Permission permission);
    Permission toPermission(PermissionCreationRequest request);

}
