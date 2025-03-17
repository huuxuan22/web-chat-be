package com.example.webchat.service;

import com.example.webchat.exception.DataNotFoundException;
import com.example.webchat.model.Chat;
import com.example.webchat.model.Users;
import com.example.webchat.repository.IChatRepository;
import com.example.webchat.repository.IUserRepository;
import com.example.webchat.request.ChatMessageRequest;
import com.example.webchat.request.GroupChatReques;
import com.example.webchat.service.impl.IChatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChatService implements IChatService {
    @Autowired
    private IChatRepository chatRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    /**
     * tạo ra 1 nhóm chat 1 là kết bạn
     * => isGroup trả về false
     * => là 1 nhóm thì trả về true
     * => user ở bên trong chinh là thông tin người dùng
     * @param userCreate
     * @param
     * @return
     */
    @Override
    @Transactional
    public Chat createChat(Users userCreate, Integer userIdAdd) {
        Users users = userRepository.findById(userIdAdd)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy bạn bèz"));
        Chat chat = new Chat();
        chat.getUsers().add(users);
        chat.getUsers().add(userCreate);
        chat.setIsGroup(0);
        return chatRepository.save(chat);
    }

    @Override
    public Chat findById(Integer id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đoạn chat"));
    }

    @Override
    public List<Chat> findAllChatByUserId(Integer userId) {
        return null;
    }

    /**
     * tao nhom chat
     * @param red
     * @param reqUser
     * @return
     */
    @Override
    @Transactional
    public Chat createGroupChat(GroupChatReques red, Users reqUser) {
        Chat group = new Chat();
        group.setIsGroup(1);
        group.setChatImage(red.getChatImage());
        group.setChatName(red.getChatName());
        group.setCreatedBy(reqUser);
        group.getAdmins().add(reqUser);
        for (Integer userId : red.getUserIds()) {
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng "));
            group.getUsers().add(user);
        }
        return chatRepository.save(group);
    }

    @Override
    @Transactional
    public Chat addUserToGroupChat(Integer userid, Integer chatId,Users repUser) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new DataNotFoundException("Đoạn chat này không tồn tại"));
        Users user = userRepository.findById(userid).orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng "));
        Boolean flag = false;
        for (Users admin : chat.getAdmins()) {
            if (repUser.equals(admin)) {
                flag = true;
            }
        }
        if (flag) {
            chat.getUsers().add(user);
        }
        return chatRepository.save(chat);
    }

    @Override
    @Transactional
    public Chat renameGroup(Integer chatId, String groupName, Users reqUser) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new DataNotFoundException("Đoạn chat này không tồn tại"));
        chat.setChatName(groupName);
        return chatRepository.save(chat);
    }

    @Override
    public void removeUserFromGroupChat(Integer userid, Integer chatId, Users reqUser) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new DataNotFoundException("Đoạn chat này không tồn tại"));
        Users userRemove = userRepository.findById(userid).orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng "));
        boolean flag = false;
        for (Users admin : chat.getAdmins()) {
            if (reqUser.equals(admin)) {
                flag = true;
            }
        }
        if (flag) {
            chat.getUsers().remove(userRemove);
        }
    }

    @Override
    @Transactional
    public void deleteChat(Integer chatId, Users repUser) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new DataNotFoundException("Đoạn chat này không tồn tại"));
        boolean flag = false;
        for (Users admin : chat.getAdmins()) {
            if (repUser.equals(admin)) {
                flag = true;
            }
        }
        if (flag) {
           chatRepository.delete(chat);
        }
    }

    @Override
    public List<ChatMessageRequest> getAllChatMessages(Integer userId,String name) {
        List<ChatMessageRequest> chatMessageRequests = new ArrayList<>();
        List<Object[]> results = chatRepository.findRecentChatsNative(userId,name);
        for (Object[] row : results) {
            ChatMessageRequest dto = new ChatMessageRequest();
            dto.setChatId((Integer) row[0]);  // Cột 1: chat_id
            dto.setChatName((String) row[1]); // Cột 2: chat_name
            dto.setIsGroup((Integer) row[2]);   // Cột 3: is_group
            dto.setMessageId((Integer) row[3]);  // Cột 4: message_id
            dto.setLastMessage((String) row[4]); // Cột 5: last_message
            dto.setLastMessageTime(((Timestamp) row[5]).toLocalDateTime()); // Cột 6: last_message_time
            dto.setSenderId((Integer) row[6]);  // Cột 7: sender_id
            dto.setSenderName((String) row[7]); // Cột 8: sender_name
            chatMessageRequests.add(dto);
        }
        return chatMessageRequests;
    }

    @Override
    public Chat findByDoubleUserId(Integer userId1, Integer userId2) {
        return chatRepository.findByDoubleUserId(userId1,userId2);
    }


}
