package com.example.webchat.controller;

import com.example.webchat.model.Message;
import com.example.webchat.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class RealTimeChat {
    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/message")
    public Message sendMessage(@Payload Message message) {
        Logger logger = LoggerFactory.getLogger(ChatController.class);
        logger.info("📩 Tin nhắn nhận được: Sender = {}, Content = {}", message);
        if (message.getChat() != null) {
            template.convertAndSend("/group/" + message.getChat().getChatId(), message);
        }
        return message;
    }







}
