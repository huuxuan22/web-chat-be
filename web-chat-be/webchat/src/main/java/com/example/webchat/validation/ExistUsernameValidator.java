package com.example.webchat.validation;

import com.example.webchat.repository.IUserRepository;
import com.example.webchat.service.impl.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistUsernameValidator implements ConstraintValidator<ExistUsername,String>{
    @Autowired
    private IUserRepository userRepository;

    @Override
    public void initialize(ExistUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if (userRepository.findByUsername(username).isPresent()) {
            return false;
        }
        return true;
    }
}
