package com.example.webchat.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMessage {
    @Column(name = "message_id")
    private Integer messageId;
    private String content;
    private Timestamp timestamp;
    private String fullName;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Integer users;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;


}
