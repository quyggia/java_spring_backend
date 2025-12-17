package com.nnq.ketnoidatabase.dto.response;


import com.nnq.ketnoidatabase.entity.Permission;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

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
