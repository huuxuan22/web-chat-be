package com.example.webchat.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invation {
    private String userSend;
    private String userReceive;
    private String message;
}
