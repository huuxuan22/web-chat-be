package com.example.webchat.validation;

import com.example.webchat.repository.IUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistUserByFullnameValidator implements ConstraintValidator<ExistUserByFullname, String> {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public void initialize(ExistUserByFullname constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (userRepository.findByFullname(string).isPresent()) {
            return false;
        }
        return true;
    }
}
