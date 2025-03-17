package com.example.webchat.controller;

import com.example.webchat.model.Users;
import com.example.webchat.service.NotificationService;
import com.example.webchat.service.impl.INotificationService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    private INotificationService notificationService;
    @GetMapping("")
    public ResponseEntity<?> getAllNotifiCationByUserId(@AuthenticationPrincipal Users users) {
        if (users == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("người dùng không tồn tại");
        }

        return ResponseEntity.status(HttpStatus.OK).body(notificationService.findAllByReceiverId(users.getUserId()));
    }


    @GetMapping("/delete-notify")
    public ResponseEntity<?> deleteNotification(
                                                @RequestParam Integer senderId,
                                                @RequestParam Integer receiverId) {

        notificationService.deleteNotification(senderId, receiverId);
        return ResponseEntity.status(HttpStatus.OK).body("OK`");
    }
}
