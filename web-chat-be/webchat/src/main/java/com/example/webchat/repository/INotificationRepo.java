package com.example.webchat.repository;

import com.example.webchat.model.Notification;
import com.example.webchat.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface INotificationRepo extends JpaRepository<Notification, Long> {
    @Modifying
    @Query(value = "INSERT INTO notifications (type, message, is_read, sender_id, receiver_id) " +
            "VALUES (:type, :message, :isRead, :senderId, :receiverId)", nativeQuery = true)
    void insertNotifications(@Param("type") String type,
                            @Param("message") String message,
                            @Param("isRead") Boolean isRead,
                            @Param("senderId") Integer senderId,
                            @Param("receiverId") Integer receiverId);

    @Query(value = "select n.* from notifications as n where n.receiver_id = :receiverId order by n.notification_id desc ",nativeQuery = true)
    List<Notification> findAllByReceiverId(@Param("receiverId") Integer receiverId);
    @Modifying
    @Transactional
    @Query(value = "delete from notifications as n\n" +
            "            where ((n.sender_id = :senderId and n.receiver_id = :receiverId)\n" +
            "            or (n.sender_id = :receiverId and n.receiver_id = :senderId))\n" +
            "            and n.is_read = 0",nativeQuery = true)
    void deleteNotificationBySenderAndReceiver(@Param("senderId") Integer senderId,@Param("receiverId") Integer receiverId);
}
