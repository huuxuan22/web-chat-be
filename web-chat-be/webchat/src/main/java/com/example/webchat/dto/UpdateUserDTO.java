package com.example.webchat.dto;

import com.example.webchat.validation.ExistUserByFullname;
import com.example.webchat.validation.ExistUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserDTO {
    @NotBlank(message = "Họ và tên không được để trống")
    @ExistUserByFullname
    private String fullName;


    private String password;
}
