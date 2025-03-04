package com.example.webchat.validation;

import com.example.webchat.repository.IUserRepository;
import com.example.webchat.service.impl.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class NotExistFullNameValidator implements ConstraintValidator<NotExistFullname, String> {
    @Autowired
    private IUserRepository userRepository;
    @Override
    public void initialize(NotExistFullname constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (userRepository.findByFullname(string) == null) {
            return false;
        }
        return true;
    }
}
