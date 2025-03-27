package com.example.webchat.repository;

import com.example.webchat.model.Chat;
import com.example.webchat.model.Users;
import com.example.webchat.request.OpptionUsers;
import com.example.webchat.request.SearchUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    @Query(value = "select distinct\n" +
            "    c.chat_id,\n" +
            "    c.chat_name,\n" +
            "    c.is_group,\n" +
            "    m.message_id,\n" +
            "    m.content as last_message,\n" +
            "    m.timestamp as last_message_time,\n" +
            "    u.user_id as sender_id,\n" +
            "    u.full_name as sender_name,\n" +
            "    c.user_id as create_by,\n" +
            "    c.chat_image\n" +
            "from\n" +
            "    chat c\n" +
            "inner join\n" +
            "    chat_users cu on c.chat_id = cu.chat_chat_id\n" +
            "left join\n" +
            "    message m on m.message_id = (\n" +
            "        select m1.message_id\n" +
            "        from message m1\n" +
            "        where m1.chat_id = c.chat_id\n" +
            "        order by m1.timestamp desc\n" +
            "        limit 1\n" +
            "    )\n" +
            "left join\n" +
            "    users u on m.user_id = u.user_id \n" +
            "where\n" +
            "    cu.users_user_id = :userId\n" +
            "    and (c.is_group in (1,2))\n" +
            "    and (\n" +
            "        (c.is_group = 2 and (u.full_name like concat('%',:name,'%') or u.full_name is null))\n" +
            "        or\n" +
            "        (c.is_group = 1 and c.chat_name like concat('%',:name,'%'))\n" +
            "    )\n" +
            "order by\n" +
            "    m.timestamp ASC;", nativeQuery = true)
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
    boolean existsByChatName(String chatName);

    @Modifying
    @Transactional
    @Query(value = "update Chat c set c.chatName = :chatName where c.chatId = :chatId")
    int updateChatNameByChatId(@Param("chatId") Integer chatId,@Param("chatName") String chatName);

    @Modifying
    @Transactional
    @Query(value = "update Chat c set c.chatImage = :avatar where c.chatId = :chatId")
    int updateAvatarChat (@Param("chatId") Integer chatId,@Param("avatar") String avatarUrl);

    @Query(value = "SELECT DISTINCT \n" +
            "    u.user_id, \n" +
            "    u.full_name, \n" +
            "    u.thubnail, \n" +
            "    CASE \n" +
            "        WHEN ca.admins_user_id IS NOT NULL THEN 'Quản trị' \n" +
            "        ELSE 'Người dùng' \n" +
            "    END AS type_user\n" +
            "FROM users AS u \n" +
            "INNER JOIN chat_users AS cu ON cu.users_user_id = u.user_id\n" +
            "INNER JOIN chat AS c ON c.chat_id = cu.chat_chat_id\n" +
            "LEFT JOIN chat_admins AS ca ON ca.admins_user_id = u.user_id AND ca.chat_chat_id = c.chat_id\n" +
            "WHERE c.chat_id = :chatId;", nativeQuery = true)
    List<Object[]> userListInChat(@Param("chatId") Integer chatId);

    @Query("SELECT c FROM Chat c WHERE c.chatName = :chatName")
    Chat findChatByChatName(@Param("chatName") String chatName);

}
