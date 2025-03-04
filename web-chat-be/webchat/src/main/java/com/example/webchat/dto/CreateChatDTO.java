package com.example.webchat.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateChatDTO implements Validator {
    @NotNull(message = "bạn chưa nhập người dùnga")
    private Integer userId;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

    @Override
    public Errors validateObject(Object target) {
        return Validator.super.validateObject(target);
    }
}
