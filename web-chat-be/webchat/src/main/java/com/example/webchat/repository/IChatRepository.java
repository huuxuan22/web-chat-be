package com.example.webchat.repository;

import com.example.webchat.model.Chat;
import com.example.webchat.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface IChatRepository extends JpaRepository<Chat, Integer> {
    /**
     * Lấy tất cả danh sách chat đã kết nối gần đây
     * @param userId
     * @return
     */
    @Query(value = """

            SELECT DISTINCT c.chat_id,\s
                                                    c.chat_name,\s
                                                    c.is_group,\s
                                                    m.message_id,\s
                                                    m.content AS last_message,\s
                                                    m.timestamp AS last_message_time,\s
                                                    u.user_id as sender_id,
                                                    u.full_name as senderName
                                    FROM chat c
                                    INNER JOIN chat_users cu ON c.chat_id = cu.chat_chat_id
                                    LEFT JOIN message m ON m.message_id = (
                                        SELECT m1.message_id \s
                                        FROM message m1 \s
                                        WHERE m1.chat_id = c.chat_id \s
                                        ORDER BY m1.timestamp DESC \s
                                        LIMIT 1
                                    )
                                    LEFT JOIN users u ON m.user_id = u.user_id
                                    WHERE cu.users_user_id = :userId\s
                AND c.chat_name LIKE CONCAT('%', :name, '%') \s
                ORDER BY m.timestamp ASC
        """, nativeQuery = true)
    List<Object[]> findRecentChatsNative(@Param("userId") Integer userId, @Param("name") String name);

    @Query(value = "select max(chat_id)  as chat_id from chat",nativeQuery = true)
    int findIdMaxChat();
    @Modifying
    @Query(value = "insert into chat (is_group,user_id)\n" +
            "values (3,1);",nativeQuery = true)
    void insertChatForAddFriend(@Param("userId") Integer userId, @Param("groupId") Integer groupId);

    @Query(value = "select c.* from chat_users as cu1\n" +
            "inner join chat_users as cu2 on cu2.chat_chat_id = cu1.chat_chat_id\n" +
            "inner join chat as c on c.chat_id = cu1.chat_chat_id\n" +
            "where cu1.users_user_id = :userId1\n" +
            "and cu2.users_user_id = :userId2\n" +
            "and is_group = 3",nativeQuery = true)
    Chat findByDoubleUserId(@Param("userId1") Integer userId1, @Param("userId2") Integer userId2);
    @Modifying
    @Query(value = "delete from chat_users as cu where cu.users_user_id  = :userId and cu.chat_chat_id = :chatId",nativeQuery = true)
    void deleteChatUserByUserIdAAndChatId(@Param("userId") Integer userId,@Param("chatId")Integer chatId);
}
