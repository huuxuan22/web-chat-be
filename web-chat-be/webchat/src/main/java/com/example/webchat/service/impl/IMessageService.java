package com.example.webchat.service.impl;

import com.example.webchat.dto.SendMessDTO;
import com.example.webchat.model.Message;
import com.example.webchat.repository.MessageProjection;
import com.example.webchat.request.ListMessageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMessageService {
    Message sendMessage(SendMessDTO sendMessDTO);
    Page<MessageProjection> getChatMessages(Integer chatId, Pageable pageable);
    Message findMessageById(Integer messageId);
    void deleteMessageById(Integer messageId);
    List<ListMessageRequest> getListMessageRequests(Integer chatId, String timestamp);
}
