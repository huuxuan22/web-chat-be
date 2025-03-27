package com.example.webchat.validation.chat_validation;

import com.example.webchat.repository.IChatRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistChatNameValidator implements ConstraintValidator<ExistChatName, String> {
    @Autowired
    private IChatRepository chatRepository;

    @Override
    public void initialize(ExistChatName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String chatName, ConstraintValidatorContext constraintValidatorContext) {
        if (chatName == null || chatName.isEmpty()) {
            return false;
        }else if (chatRepository.existsByChatName(chatName)) {
            return false;
        }
        return true;
    }
}
