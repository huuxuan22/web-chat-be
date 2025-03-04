package com.example.webchat.dto;

import com.example.webchat.validation.NotExistUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO implements Validator {
    @Email(message = "Bạn cần điền đúng định dạng email")
    @NotExistUsername
    private String username;

    @NotBlank(message = "Bạn chưa điền mật khẩu")
    private String password;


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
