package com.example.webchat.service;

import com.example.webchat.dto.SendMessDTO;
import com.example.webchat.exception.DataNotFoundException;
import com.example.webchat.model.Message;
import com.example.webchat.repository.IChatRepository;
import com.example.webchat.repository.IMessageRepository;
import com.example.webchat.repository.IUserRepository;
import com.example.webchat.request.ListMessageRequest;
import com.example.webchat.service.impl.IMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService implements IMessageService {
    @Autowired
    private IMessageRepository messageRepository;
    @Autowired
    private IChatRepository chatRepository;
    @Autowired
    private IUserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
    @Override
    public Message sendMessage(SendMessDTO sendMessDTO) {
        Message message = Message.builder()
                .chat(chatRepository.findById(sendMessDTO.getChatId()).orElse(null))
                .users(userRepository.findById(sendMessDTO.getUserId()).orElse(null))
                .content(sendMessDTO.getContent())
                .timestamp(Timestamp.from(Instant.now()))
                .build();
        logger.info("Loi trong luu tin nhan: {} ", messageRepository.save(message));
        return null;
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

    @Override
    public List<ListMessageRequest> getListMessageRequests(Integer chatId, String timestamp) {
        List<Object[]> messages;

        if (timestamp == null || timestamp.equals("")) {
            messages = messageRepository.findLatestMessages(Long.valueOf(chatId));
        } else {
            messages = messageRepository.findOlderMessages(Long.valueOf(chatId), Timestamp.valueOf(timestamp));
        }

        return convertToListMessageRequest(messages);
    }

    private List<ListMessageRequest> convertToListMessageRequest(List<Object[]> messages) {
        List<ListMessageRequest> messageRequests = new ArrayList<>();

        for (int i = messages.size() - 1; i >= 0; i--) { // Duyệt ngược
            Object[] obj = messages.get(i);
            String fullName = obj[0].toString();
            String messageId = obj[1].toString();
            String content = obj[2].toString();
            String timestamp = obj[3].toString();
            String chatId = obj[4].toString();
            String userId = obj[5].toString();

            ListMessageRequest messageRequest = ListMessageRequest.builder()
                    .fullName(fullName)
                    .messageId(Integer.valueOf(messageId))
                    .content(content)
                    .timestamp(Timestamp.valueOf(timestamp))
                    .chatId(Integer.valueOf(chatId))
                    .userId(Integer.valueOf(userId))
                    .build();

            messageRequests.add(messageRequest);
        }
        return messageRequests;
    }

}
