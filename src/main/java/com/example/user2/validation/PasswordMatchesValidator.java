package com.example.user2.validation;

import com.example.user2.customAnnatation.PasswordMatches;
import com.example.user2.dto.UserRegisterDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserRegisterDto> {
    @Override
    public boolean isValid(UserRegisterDto value, ConstraintValidatorContext context) {
        if (value.getPassword() == null || value.getConfirmPassword() == null) {
            return false;
        }
        return value.getPassword().equals(value.getConfirmPassword());
    }
}
