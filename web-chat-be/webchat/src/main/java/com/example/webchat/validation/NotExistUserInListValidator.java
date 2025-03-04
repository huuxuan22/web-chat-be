package com.example.webchat.validation;

import com.example.webchat.repository.IUserRepository;
import com.example.webchat.service.impl.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class NotExistUserInListValidator implements ConstraintValidator<NotExistUserInList, List<Integer> > {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public boolean isValid(List<Integer> userIds, ConstraintValidatorContext constraintValidatorContext) {
        boolean flag = true;
        for (Integer userId : userIds) {
            if (!userRepository.existsById(userId)) {
                flag = false;
            }
        }
        if (flag) {
            return true;
        }
        return false;
    }
}
