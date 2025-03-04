package com.example.webchat.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Integer chatId;

    @Column(name = "chat_name")
    private String chatName;

    @Column(name = "chat_image")
    private String chatImage;

    @Column(name = "is_group")
    private Boolean isGroup;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users createdBy;

    @ManyToMany
    private Set<Users> admins = new HashSet<>();

    @ManyToMany
    private Set<Users> users = new HashSet<>();

    @OneToMany
    private List<Message> messages;
}
