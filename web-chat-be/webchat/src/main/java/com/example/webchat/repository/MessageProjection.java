package com.example.webchat.repository;

import com.example.webchat.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface MessageProjection {
    Integer getUserId();
    String getFullName();
    Integer getMessageId();
    String getContent();
    LocalDateTime getTimestamp();
    @Query(value = "" +
            "SELECT u.full_name,m.* " +
            "FROM chat AS c " +
            "INNER JOIN message AS m ON m.chat_id = c.chat_id " +
            "LEFT JOIN users u ON u.user_id = m.user_id " +
            "WHERE c.chat_id = :chatId " +
            "ORDER BY m.timestamp desc ", nativeQuery = true)
    Page<Message> findAllByChatId(@Param("chatId") Integer chatId, Pageable pageable);


    // find by chat id
    // Lấy 20 tin nhắn mới nhất trong một cuộc trò chuyện
    @Query(value = "" +
            "SELECT u.full_name,m.* " +
            "FROM chat AS c " +
            "INNER JOIN message AS m ON m.chat_id = c.chat_id " +
            "LEFT JOIN users u ON u.user_id = m.user_id " +
            "WHERE c.chat_id = :chatId " +
            "ORDER BY m.timestamp desc " +
            "LIMIT 15;", nativeQuery = true)
    List<Object[]> findLatestMessages(@Param("chatId") Long chatId);
    // Lấy 20 tin nhắn cũ hơn một mốc thời gian
    @Query(value = "SELECT u.full_name, m.message_id, m.content, m.timestamp, m.chat_id, m.user_id " +
            "FROM chat AS c " +
            "INNER JOIN message AS m ON m.chat_id = c.chat_id " +
            "LEFT JOIN users u ON u.user_id = m.user_id " +
            "WHERE c.chat_id = :chatId AND m.timestamp < :lastTimestamp " +
            "ORDER BY m.timestamp desc " +
            "LIMIT 15", nativeQuery = true)
    List<Object[]> findOlderMessages(@Param("chatId") Long chatId, @Param("lastTimestamp") Timestamp lastTimestamp);
}
