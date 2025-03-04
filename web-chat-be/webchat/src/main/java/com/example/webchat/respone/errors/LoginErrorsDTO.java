package com.example.webchat.respone.errors;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginErrorsDTO {
    private String username;
    private String password;
}
