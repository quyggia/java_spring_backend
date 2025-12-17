package com.nnq.ketnoidatabase.dto.request;

import com.nnq.ketnoidatabase.validator.DobConstrain;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;


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
