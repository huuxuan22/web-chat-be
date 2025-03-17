package com.example.webchat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageRequest {
    @JsonProperty(namespace = "chat_id")
    private Integer chatId;
    @JsonProperty(namespace = "chat_name")
    private String chatName;
    @JsonProperty(namespace = "is_group")
    private Integer isGroup;
    @JsonProperty(namespace = "message_id")
    private Integer messageId;
    @JsonProperty(namespace = "last_message")
    private String lastMessage;
    @JsonProperty(namespace = "last_message_time")
    private LocalDateTime lastMessageTime;
    @JsonProperty(namespace = "sender_id")
    private Integer senderId;
    @JsonProperty(namespace = "sender_name")
    private String senderName;
}
