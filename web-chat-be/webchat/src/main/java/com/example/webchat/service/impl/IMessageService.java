package com.example.webchat.service.impl;

import com.example.webchat.dto.SendMessDTO;
import com.example.webchat.model.Message;
import com.example.webchat.request.ListMessageRequest;

import java.sql.Timestamp;
import java.util.List;

public interface IMessageService {
    Message sendMessage(SendMessDTO sendMessDTO);
    List<Message> getChatMessages(Integer chatId);
    Message findMessageById(Integer messageId);
    void deleteMessageById(Integer messageId);
    List<ListMessageRequest> getListMessageRequests(Integer chatId, String timestamp);
}
