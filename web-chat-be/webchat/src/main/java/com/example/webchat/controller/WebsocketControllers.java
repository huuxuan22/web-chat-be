package com.example.webchat.controller;

import com.example.webchat.component.JwtTokenUtils;
import com.example.webchat.dto.SendMessDTO;
import com.example.webchat.model.Chat;
import com.example.webchat.model.Notification;
import com.example.webchat.model.Users;
import com.example.webchat.request.Invation;
import com.example.webchat.request.MessageSend;
import com.example.webchat.service.impl.IChatService;
import com.example.webchat.service.impl.IMessageService;
import com.example.webchat.service.impl.INotificationService;
import com.example.webchat.service.impl.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketControllers {
    @Autowired
    private IMessageService messageService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private INotificationService notificationService;
    @Autowired
    private IChatService chatService;

    @Autowired
    private IUserService userService;
    private static final Logger logger = LoggerFactory.getLogger(WebsocketControllers.class);

    @MessageMapping("/chat") // Client gửi tin nhắn đến /app/chat
    @SendTo("/topic/messages") // Tin nhắn được gửi đến tất cả ai đang subscribe /topic/messages
    public MessageSend sendMessage(@Payload MessageSend send) {
        messageService.sendMessage(SendMessDTO.builder()
                        .chatId(Integer.valueOf(send.getReceiver()))
                        .content(send.getData())
                        .userId(Integer.valueOf(send.getUserSend()))
                .build());
        logger.info("📩 Tin nhắn nhận được: {} ", send.getData());
        logger.info("📩 Nguoi nhan: {} ", send.getReceiver());
        logger.info("📩 Nguoi gui: {}", send.getUserSend());
        return send; // Trả về tin nhắn để gửi đến client
    }

    /**
     * gửi lời mời kết bạn
     * @param send
     * @return
     */
    @MessageMapping("/notification-add")
    @SendTo("/topic/messages")
    public Invation sendNotificationAddFriend(@Payload Invation send) {
        notificationService.sendNotification(
                "kết bạn",
                "đã gửi lời mời kết bạn ",
                false,
                Integer.valueOf(send.getUserSend()),
                Integer.valueOf(send.getUserReceive()));
        logger.info("📩 Nguoi nhan: {} ", send.getUserReceive());
        logger.info("📩 Nguoi gui: {}", send.getUserSend());
        logger.info("📩 Mess: {}", send.getMessage());
        return send;
    }


    @MessageMapping("/cancel-notification-add")
    @SendTo("/topic/messages")
    public Invation cancelAddFriend(@Payload Invation send) {
        notificationService.deleteNotification(
                Integer.valueOf(send.getUserSend()),
                Integer.valueOf(send.getUserReceive()));
        logger.info("📩 Nguoi nhan: {} ", send.getUserReceive());
        logger.info("📩 Nguoi gui: {}", send.getUserSend());
        logger.info("📩 Message: {}", send.getMessage());
        return send;
    }
}
