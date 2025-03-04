package com.example.webchat.dto;

import com.example.webchat.model.Roles;
import com.example.webchat.validation.ExistUserByFullname;
import com.example.webchat.validation.ExistUsername;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO implements Validator {
    @NotBlank(message = "Họ và tên không được để trống")
    @ExistUserByFullname
    private String fullName;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 5, max = 20, message = "Tên đăng nhập phải từ 5 đến 20 ký tự")
    @ExistUsername
    @Email(message = "Bạn chưa nhập đúng định dạng email")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
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
