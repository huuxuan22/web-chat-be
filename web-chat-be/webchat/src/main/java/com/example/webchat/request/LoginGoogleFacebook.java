package com.example.webchat.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginGoogleFacebook {
    private String fullName;
    private String username;
}
