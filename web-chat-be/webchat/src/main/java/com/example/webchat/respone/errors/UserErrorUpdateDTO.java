package com.example.webchat.respone.errors;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserErrorUpdateDTO {
    private String fullName;
    private String password;
}
