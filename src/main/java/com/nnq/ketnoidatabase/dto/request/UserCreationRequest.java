package com.nnq.ketnoidatabase.dto.request;

import com.nnq.ketnoidatabase.validator.DobConstrain;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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
    LocalDate dob;


}
