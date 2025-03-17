package com.example.webchat.repository;

import com.example.webchat.model.Users;
import com.example.webchat.respone.UserAddFiend;
import org.hibernate.Internal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<Users, Integer> {
    @Query("select u from  Users u where u.username = :username")
    Optional<Users> findByUsername(@Param("username") String username);

    @Query("select u from Users u where u.username = :username")
    Optional<Users> findByEmail(@Param("username") String username);

    @Query("select u from Users u where u.fullName like concat('%', :param, '%')")
    List<Users> findListByFullName(@Param("param") String param);

    @Query("select u from Users  u where u.fullName = :fullname")
    Optional<Users> findByFullname(@Param("fullname") String fullname);

    @Query("select u from Users  u where u.fullName like concat('%',:fullName,'%')")
    List<Users> searchByFullname(@Param("fullName") String fullname);

    @Query(value = "SELECT DISTINCT \n" +
            "    u.user_id,\n" +
            "    u.full_name,\n" +
            "    u.thubnail,\n" +
            "    u.username,\n" +
            "    CASE \n" +
            "        WHEN COALESCE(c.is_group, 4) = 1 THEN 4\n" +
            "        WHEN COALESCE(c.is_group, 4) = 3 THEN 3\n" +
            "        ELSE COALESCE(c.is_group, 4)\n" +
            "    END AS is_group\n" +
            "FROM users AS u\n" +
            "LEFT JOIN (\n" +
            "    SELECT \n" +
            "        cu2.users_user_id AS user_id,\n" +
            "        c.is_group\n" +
            "    FROM chat c\n" +
            "    JOIN chat_users cu1 ON cu1.chat_chat_id = c.chat_id\n" +
            "    JOIN chat_users cu2 ON cu2.chat_chat_id = c.chat_id\n" +
            "    WHERE cu1.users_user_id = :userLogin \n" +
            "      AND cu2.users_user_id != :userLogin\n" +
            "      AND c.is_group IN (2,3)\n" +
            ") AS c ON u.user_id = c.user_id\n" +
            "WHERE u.user_id != :userLogin\n" +
            "  AND u.full_name LIKE CONCAT('%', :fullName, '%')\n" +
            "GROUP BY u.user_id, u.full_name, u.username, c.is_group;\n" +
            "            ", nativeQuery = true)
    List<Object[]> searchUsersByFullname(@Param("fullName") String fullName,@Param("userLogin") Integer  userLogin);

    @Modifying
    @Query(value = "insert into chat_users  (chat_chat_id,users_user_id)" +
            "values (:chatId,:userId)", nativeQuery = true)
    void insertToChatUsers(@Param("userId") Integer userId, @Param("chatId") Integer chatId);

}
