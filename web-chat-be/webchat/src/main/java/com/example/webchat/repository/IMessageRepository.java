package com.example.webchat.repository;

import com.example.webchat.model.Chat;
import com.example.webchat.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT  u.user_id,u.full_name AS fullName, m.message_id AS messageId, m.content AS content, m.timestamp AS timestamp " +
            "FROM chat AS c " +
            "INNER JOIN message AS m ON m.chat_id = c.chat_id " +
            "LEFT JOIN users u ON u.user_id = m.user_id " +
            "WHERE c.chat_id = :chatId " +
            "ORDER BY m.timestamp desc ",
            countQuery = "SELECT COUNT(*) FROM message m WHERE m.chat_id = :chatId", // Để phân trang
            nativeQuery = true)
    Page<MessageProjection> findAllByChatId(@Param("chatId") Integer chatId, Pageable pageable);


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
