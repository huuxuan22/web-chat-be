package com.example.webchat.repository;

import com.example.webchat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Long> {
    @Query("select m from Message m join m.chat c where c.chatId = :chatId")
    List<Message> findAllById(@Param("chatId") Integer chatId);
    // find by chat id
}
