package com.example.webchat.request;

import com.example.webchat.model.Chat;
import com.example.webchat.model.Users;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListMessageRequest {
    @JsonProperty(namespace = "full_name")
    private String fullName;
    @JsonProperty(namespace = "message_id")
    private Integer messageId;
    @JsonProperty(namespace = "content")
    private String content;
    @JsonProperty(namespace = "timestamp")
    private Timestamp timestamp;
    @JsonProperty(namespace = "chat_id")
    private Integer chatId;
    @JsonProperty(namespace = "user_id")
    private Integer userId;
}
