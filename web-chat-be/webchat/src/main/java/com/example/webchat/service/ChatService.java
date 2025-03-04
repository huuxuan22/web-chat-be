package com.example.webchat.service;

import com.example.webchat.exception.DataNotFoundException;
import com.example.webchat.model.Chat;
import com.example.webchat.model.Users;
import com.example.webchat.repository.IChatRepository;
import com.example.webchat.repository.IUserRepository;
import com.example.webchat.request.GroupChatReques;
import com.example.webchat.service.impl.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class ChatService implements IChatService {
    @Autowired
    private IChatRepository chatRepository;

    @Autowired
    private IUserRepository userRepository;


    /**
     * tạo ra 1 nhóm chat 1 là kết bạn
     * => isGroup trả về false
     * => là 1 nhóm thì trả về true
     * => user ở bên trong chinh là thông tin người dùng
     * @param userCreate
     * @param fullName
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
        chat.setIsGroup(false);
        return chatRepository.save(chat);
    }

    @Override
    public Chat findById(Integer id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đoạn chat"));
    }

    @Override
    public List<Chat> findAllChatByUserId(Integer userId) {
        return chatRepository.findAllById(Collections.singleton(userId));
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
        group.setIsGroup(true);
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
}
