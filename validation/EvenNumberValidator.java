package com.example.user2.validation;

import com.example.user2.customAnnatation.EvenNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EvenNumberValidator implements ConstraintValidator<EvenNumber, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        //Null olabilen alanın üzerinde yazılabilir ve ayrıca tek bir görevi olamalı @Notnull null a bakmalı bu çift sayıya bakmalı.
        if (value == null) return true;

        //Çift sayıysa true dönüp hata vermiycek . Tek sayıysa false dönüp hata vericek
        return value % 2 == 0;
    }
}
