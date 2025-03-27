package com.example.webchat.controller;

import com.example.webchat.model.Users;
import com.example.webchat.service.NotificationService;
import com.example.webchat.service.impl.INotificationService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    @DeleteMapping("clean")
    public ResponseEntity<?> cleanNotification(@AuthenticationPrincipal Users users,
                                               @RequestParam Integer notificationIds) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(() -> {
                notificationService.deleteUnreadNotifications(notificationIds);
            },1, TimeUnit.MINUTES);
            return ResponseEntity.status(HttpStatus.OK).body("OK`");
    }
}
