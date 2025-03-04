package com.example.webchat.validation;

import com.example.webchat.repository.IUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class NotExistUsernameValidator implements ConstraintValidator<NotExistUsername, String> {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (userRepository.findByUsername(string) != null) {
            return true;
        }
        return false;
    }
}
