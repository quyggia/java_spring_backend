package com.nnq.ketnoidatabase.dto.response;

import java.util.Set;

import com.nnq.ketnoidatabase.entity.Permission;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoleResponse {

    String name;
    String description;
    Set<Permission> permissions;
}
