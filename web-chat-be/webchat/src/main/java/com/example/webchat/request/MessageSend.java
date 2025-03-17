package com.example.webchat.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageSend {
    private String data;
    private String userSend;
    private String receiver;
}
