package com.nnq.ketnoidatabase.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nnq.ketnoidatabase.dto.request.RoleCreationRequest;
import com.nnq.ketnoidatabase.dto.response.RoleResponse;
import com.nnq.ketnoidatabase.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreationRequest request);
}
