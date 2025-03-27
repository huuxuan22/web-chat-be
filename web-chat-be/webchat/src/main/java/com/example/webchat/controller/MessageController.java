package com.example.webchat.controller;

import com.example.webchat.dto.SendMessDTO;
import com.example.webchat.model.Message;
import com.example.webchat.model.Users;
import com.example.webchat.repository.MessageProjection;
import com.example.webchat.request.ListMessageRequest;
import com.example.webchat.respone.errors.SendMessErrors;
import com.example.webchat.service.impl.IChatService;
import com.example.webchat.service.impl.IMessageService;
import com.example.webchat.service.impl.IUserService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IChatService chatService;
    @Autowired
    private IMessageService messageService;

    @PostMapping("/create")
    public ResponseEntity<?> sendMessage(@AuthenticationPrincipal Users users,
                                         @Valid @RequestBody SendMessDTO sendMessDTO,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            SendMessErrors sendMessErrors = new SendMessErrors();
            bindingResult.getFieldErrors()
                    .stream().forEach(error -> {
                        String field = error.getField();
                        String message = error.getDefaultMessage();
                        switch (field) {
                            case "chatId":
                                sendMessErrors.setChatId(
                                        sendMessDTO.getChatId() == null ? message :
                                                sendMessDTO.getChatId() + "; " + message
                                );
                                break;
                            case "userId":
                                sendMessErrors.setUserId(
                                        sendMessDTO.getUserId() == null ? message :
                                                sendMessDTO.getUserId() + "; " + message
                                );
                                break;
                            case "password":
                                sendMessErrors.setContent(
                                        sendMessDTO.getContent() == null ? message :
                                                sendMessDTO.getContent() + "; " + message
                                );
                                break;
                        }
                    });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sendMessErrors);
        }
        messageService.sendMessage(sendMessDTO);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/chat")
    public ResponseEntity<?> getChat(@AuthenticationPrincipal Users users,
                                     @RequestParam("chatId") Integer chatId,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "15") Integer size) {
        if (chatId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("đoạn chat này không tồn tại ");
        }
        Pageable pageable = PageRequest.of(page, size,Sort.by("timestamp").descending());
        Page<MessageProjection> messages = messageService.getChatMessages(chatId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(messages.getContent());
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<?> getMessage(@AuthenticationPrincipal Users users,
                                        @PathVariable("messageId") Integer messageId) {
        if (messageId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("tin nhắn này không đúng định dạng");
        }
        Message message = messageService.findMessageById(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(message.getContent());
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage( @AuthenticationPrincipal Users users
                                            ,@PathVariable("messageId") Integer messageId) {
        if (messageId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("tin nhắn này không đúng định dạng");
        }
        messageService.deleteMessageById(messageId);
        return ResponseEntity.ok().build();
    }
}
