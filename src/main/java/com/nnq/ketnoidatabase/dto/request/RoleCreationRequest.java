package com.nnq.ketnoidatabase.dto.request;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
