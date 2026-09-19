package com.example.user2.validation;

import com.example.user2.customAnnatation.NotAdmin;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotAdminValidator implements ConstraintValidator<NotAdmin, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //Null kontrolü @NotNull ' bırakılmalı bu yüzden null 'ı atlıyoruz.
        if (value == null) {
            return true;
        }
        return !value.equalsIgnoreCase("admin");
    }
}
