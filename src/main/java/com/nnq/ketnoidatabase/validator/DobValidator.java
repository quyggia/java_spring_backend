package com.nnq.ketnoidatabase.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DobValidator implements ConstraintValidator<DobConstrain, LocalDate> {


    private int min;
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {

        if(Objects.isNull(localDate))
            return true;

        long year = ChronoUnit.YEARS.between(localDate, LocalDate.now());


        return year >= min;
    }

    @Override
    public void initialize(DobConstrain constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }
}
