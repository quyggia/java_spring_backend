package com.nnq.ketnoidatabase.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nnq.ketnoidatabase.validator.DobConstrain;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 3, message = "USERNAME_SIZE")
    String username;

    @Size(min = 5, message = "PASSWORD_SIZE")
    String password;

    String firstname;
    String lastname;

    @DobConstrain(min = 18, message = "INVALIDATE_DOB")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;
}
