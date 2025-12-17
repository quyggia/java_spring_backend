package com.nnq.ketnoidatabase.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {DobValidator.class}
)
public @interface DobConstrain {
    String message() default "Tuổi của người dùng không hợp lệ!";

    int min();
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
