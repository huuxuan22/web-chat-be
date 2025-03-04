package com.example.webchat.service;

import com.example.webchat.dto.SendMessDTO;
import com.example.webchat.exception.DataNotFoundException;
import com.example.webchat.model.Message;
import com.example.webchat.repository.IChatRepository;
import com.example.webchat.repository.IMessageRepository;
import com.example.webchat.repository.IUserRepository;
import com.example.webchat.service.impl.IMessageService;
import com.example.webchat.service.impl.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService implements IMessageService {
    @Autowired
    private IMessageRepository messageRepository;
    @Autowired
    private IChatRepository chatRepository;
    @Autowired
    private IUserRepository userRepository;

    @Override
    public Message sendMessage(SendMessDTO sendMessDTO) {
        Message message = Message.builder()
                .chat(chatRepository.findById(sendMessDTO.getChatId()).orElse(null))
                .users(userRepository.findById(sendMessDTO.getUserId()).orElse(null))
                .content(sendMessDTO.getContent())
                .timestamp(LocalDateTime.now())
                .build();
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getChatMessages(Integer chatId) {
        return messageRepository.findAllById(chatId);
    }

    @Override
    public Message findMessageById(Integer messageId) {
        return messageRepository.findById(Long.valueOf(messageId))
                .orElseThrow(() ->new DataNotFoundException("Không tìm thấy đoạn tin nhắn "));
    }

    @Override
    public void deleteMessageById(Integer messageId) {
        Message message = messageRepository.findById(Long.valueOf(messageId))
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đoạn tin nhắn "));
        messageRepository.deleteById(Long.valueOf(messageId));
    }
}
