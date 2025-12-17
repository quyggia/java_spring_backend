package com.nnq.ketnoidatabase.mapper;

import com.nnq.ketnoidatabase.dto.request.UserCreationRequest;
import com.nnq.ketnoidatabase.dto.request.UserUpdateRequest;
import com.nnq.ketnoidatabase.dto.response.UserResponse;
import com.nnq.ketnoidatabase.entity.User;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);

    User toUser(UserCreationRequest request);


    @Mapping(target = "roles", ignore = true)
    void updateMapper(UserUpdateRequest request,@MappingTarget User user);
}
