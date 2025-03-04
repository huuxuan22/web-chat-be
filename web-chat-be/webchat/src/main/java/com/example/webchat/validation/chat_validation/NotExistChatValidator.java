package com.example.webchat.validation.chat_validation;

import com.example.webchat.repository.IChatRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class NotExistChatValidator implements ConstraintValidator<NotExistChat, Integer> {
    @Autowired
    private IChatRepository chatRepository;
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
       if (chatRepository.existsById(integer)) {
           return true;
       }
        return false;
    }
}
