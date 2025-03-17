package com.example.webchat.service.impl;

import com.example.webchat.model.Notification;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface INotificationService {
    void sendNotification(String type, String message, Boolean isRead, Integer sender, Integer receiver);
    List<Notification> findAllByReceiverId(@Param("receiverId") Integer receiverId);
    void deleteNotification(Integer sender, Integer receiver);
}
