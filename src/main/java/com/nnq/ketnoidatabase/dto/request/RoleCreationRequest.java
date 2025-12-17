package com.nnq.ketnoidatabase.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoleCreationRequest {
    String name;
    String description;
    Set<String> permissions;
}
