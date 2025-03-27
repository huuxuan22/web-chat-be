package com.example.webchat.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateChatName {
    private Integer chatId;
    private String chatName;

}
