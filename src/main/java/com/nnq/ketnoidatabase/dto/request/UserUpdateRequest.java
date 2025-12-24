package com.nnq.ketnoidatabase.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.nnq.ketnoidatabase.validator.DobConstrain;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    String password;
    String firstname;
    String lastname;

    @DobConstrain(min = 18)
    LocalDate dob;

    List<String> roles;
}
