package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MinDateValidator implements ConstraintValidator<MinDateValue, LocalDate> {

    private LocalDate minDate;

    @Override
    public void initialize(MinDateValue validDate) {
        this.minDate = LocalDate.parse(validDate.minDate(), DateTimeFormatter.ISO_DATE);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
        if (value != null) {
            return !value.isBefore(minDate);
        }
        return false;
    }
}