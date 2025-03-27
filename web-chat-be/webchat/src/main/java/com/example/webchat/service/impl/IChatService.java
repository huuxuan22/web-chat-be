package com.example.webchat.service.impl;

import com.example.webchat.model.Chat;
import com.example.webchat.model.Users;
import com.example.webchat.request.ChatMessageRequest;
import com.example.webchat.request.GroupChatReques;
import com.example.webchat.request.SearchUser;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IChatService {
    Chat createChat(Users users, Integer userIdAdd);
    Chat findById(Integer id);
    List<Chat> findAllChatByUserId(Integer userId);
    Chat createGroupChat(GroupChatReques red, Users users);
    Chat addUserToGroupChat(Integer userid, Integer chatId, Users repUser); // chat id chinhs la nhom
    Chat renameGroup(Integer chatId, String groupName,Users repUser);
    void removeUserFromGroupChat(Integer userid, Integer chatId, Users users);
    void deleteChat(Integer chatId, Users userRed);
    List<ChatMessageRequest> getAllChatMessages(Integer userId,String name);
    Chat findByDoubleUserId(Integer userId1,Integer userId2);
    void acceptAddFriend(Integer senderId, Integer receiverId);
    int updateChatNameByChatId( Integer chatId, String chatName);
    int updateAvatarChat ( Integer chatId, String avatarUrl);
    List<SearchUser> userListInChat(@Param("chatId") Integer chatId);
    boolean findChatByChatName(String chatName);
    List<Users> findAllUsers(Integer chatId);
}

