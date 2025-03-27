package com.example.webchat.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendMessDTO implements Validator {

    @NotNull(message = "User ID không được để trống")
    @Min(value = 1, message = "User ID phải lớn hơn 0")
    private Integer userId;

    @NotNull(message = "Chat ID không được để trống")
    @Min(value = 1, message = "Chat ID phải lớn hơn 0")
    private Integer chatId;

    @NotBlank(message = "Nội dung tin nhắn không được để trống")
    private String content;

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
