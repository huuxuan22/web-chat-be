package com.example.webchat.service;

import com.example.webchat.exception.DataNotFoundException;
import com.example.webchat.model.Chat;
import com.example.webchat.model.Notification;
import com.example.webchat.model.Users;
import com.example.webchat.repository.IChatRepository;
import com.example.webchat.repository.INotificationRepo;
import com.example.webchat.repository.IUserRepository;
import com.example.webchat.service.impl.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NotificationService implements INotificationService {
    @Autowired
    private INotificationRepo notificationRepo;
    @Autowired
    private IChatRepository chatRepository;
    @Autowired
    private IUserRepository userRepository;
//    tring type,
//    @Param("message") String message,
//    @Param("isRead") Boolean isRead,
//    @Param("senderId") Long senderId,
//    @Param("receiverId") Long receiverId);

    @Override
    @Transactional
    public void sendNotification(String type, String message, Boolean isRead, Integer sender, Integer receiver) {
         notificationRepo.insertNotifications(
                type,message,isRead,sender,receiver
        );
        Users usersSend = userRepository.findById(Integer.valueOf(sender)).get();
        Users userRevice = userRepository.findById(Integer.valueOf(receiver)).get();
        Chat chat = new Chat();
        // Tạo mới Set users thay vì gọi clear()
        Set<Users> usersSet = new HashSet<>();
        usersSet.add(usersSend);
        usersSet.add(userRevice);
        chat.setUsers(usersSet);
        chat.setCreatedBy(usersSend);
        chat.setIsGroup(3);
        chatRepository.save(chat);
    }

    @Override
    public List<Notification> findAllByReceiverId(Integer receiverId) {
        return notificationRepo.findAllByReceiverId(receiverId);
    }

    @Override
    @Transactional
    public void deleteNotification(Integer sender, Integer receiver) {
        Chat chat = chatRepository.findByDoubleUserId(sender,receiver);
        if (chat == null) {
            return;
        }
        notificationRepo.deleteNotificationBySenderAndReceiver(sender,receiver);
        chatRepository.deleteChatUserByUserIdAAndChatId(sender,chat.getChatId());
        chatRepository.deleteChatUserByUserIdAAndChatId(receiver,chat.getChatId());
        chatRepository.delete(chat);
    }
}
