package com.example.webchat.respone.errors;

import com.example.webchat.validation.ExistUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserErrorsDTO {
    private String fullName;
    private String username;
    private String password;
}
