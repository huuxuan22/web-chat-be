package com.example.webchat.repository;

import com.example.webchat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMessageRepo extends JpaRepository<Message, Integer> {
    @Query("select m from Message m join m.chat c where c.chatId = :chatId")
    List<Message> findByChatId(@Param("chatId") Integer chatId);
}
