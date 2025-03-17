package com.example.webchat.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId; // Sửa lại tên biến

    private String type;
    private String message;
    private Integer isRead;

    @ManyToOne
    @JoinColumn(name = "sender_id")  // Ai gửi thông báo
    private Users sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Users receiver;
}
