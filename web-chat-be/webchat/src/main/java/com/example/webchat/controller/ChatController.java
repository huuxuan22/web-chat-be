package com.example.webchat.controller;

import com.example.webchat.dto.CreateChatDTO;
import com.example.webchat.dto.CreateGroupDTO;
import com.example.webchat.model.Chat;
import com.example.webchat.model.Users;
import com.example.webchat.request.GroupChatReques;
import com.example.webchat.request.UpdateChatName;
import com.example.webchat.respone.errors.CreateGroupErrors;
import com.example.webchat.service.impl.IChatService;
import com.example.webchat.utils.Utils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private IChatService chatService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private Utils utils;

    @PostMapping("/single")
    public ResponseEntity<?> createChatHandler(@AuthenticationPrincipal Users user,
                                               @Valid @RequestBody CreateChatDTO createChatDTO
                                               , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult);
        }
        chatService.createChat(user, createChatDTO.getUserId());
        return ResponseEntity.ok().build();
    }


    @PostMapping("/group")
    public ResponseEntity<?> createGroupHandler(@AuthenticationPrincipal Users users,
                                                @Valid @RequestBody CreateGroupDTO createGroupDTO,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            CreateGroupErrors createGroupErrors = new CreateGroupErrors();
            bindingResult.getFieldErrors().stream()
                    .forEach(fieldError -> {
                        String field = fieldError.getField();
                        String message = fieldError.getDefaultMessage();
                        switch (field) {
                            case "userIds":
                                createGroupErrors.setUserIds(
                                        createGroupErrors.getUserIds() == null ? message :
                                                createGroupErrors.getUserIds() + "; " + message
                                );
                                break;
                            case "groupName":
                                createGroupErrors.setGroupName(
                                        createGroupErrors.getGroupName() == null ? message :
                                                createGroupErrors.getGroupName() + "; " + message
                                );
                                break;
                        }
                    });
            return ResponseEntity.badRequest().body(createGroupErrors);
        }
        chatService.createGroupChat(modelMapper.map(createGroupDTO, GroupChatReques.class),users);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<?> findChatByIdHandler(@AuthenticationPrincipal Users users,
                                                 @PathVariable("chatId") String chatId) {
        Chat chat=chatService.findById(Integer.valueOf(chatId));
        return ResponseEntity.ok().body(chat.getUsers());
    }

    /**
     * lay danh sach nguoi dung
     * @param users
     * @return
     */
    @GetMapping("/user")
    public ResponseEntity<?> getChatListByUserId(@AuthenticationPrincipal Users users) {
        List<Chat> chatList = chatService.findAllChatByUserId(users.getUserId());
        return ResponseEntity.ok().body(chatList);
    }

    /**
     * them nguoi dung vao trong group
     * @param users
     * @param chatId
     * @param userId
     * @return
     */
    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<?> addUserToGroup(@AuthenticationPrincipal Users users,
                                                @PathVariable("chatId") Integer chatId,
                                                @PathVariable("userId") Integer userId) {
        if (chatId == null || userId == null) {
            return ResponseEntity.badRequest().body("người dùng hoặc nhóm chat không hợp lệ ");
        }
        chatService.addUserToGroupChat(userId,chatId,users);
        return ResponseEntity.ok().build();
    }

    /**
     * xoa 1 nguoi dung ra khoi nhom
     * @param users
     * @param chatId
     * @param userId
     * @return
     */
    @DeleteMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<?> removeUserFromGroup(@AuthenticationPrincipal Users users,
                                                 @PathVariable("chatId") Integer chatId,
                                                 @PathVariable("userId") Integer userId) {
        if (chatId == null || userId == null) {
            return ResponseEntity.badRequest().body("người dùng hoặc nhóm chat không hợp lệ ");
        }
        chatService.removeUserFromGroupChat(userId,chatId,users);
        return ResponseEntity.ok().build();
    }

    /**
     * xoa luon nhom chat
     * @param users
     * @param chatId
     * @return
     */
    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<?> removeChat(@AuthenticationPrincipal Users users,
                                        @PathVariable("chatId") Integer chatId) {
        if (chatId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("nhóm chat không hợp lệ");
        }
        chatService.deleteChat(chatId,users);
        return ResponseEntity.ok().build();
    }

    /**
     * lấy tất cả các đoạn tin nhắn ddax ket ban
     * @param users
     * @return
     */
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllChatByUserId(@AuthenticationPrincipal Users users,
                                                @RequestParam("name") String name) {
        if (users == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("người dùng không tồn tại");
        }
        if (name == null || name.isEmpty()) {
            name = "";
        }
        return ResponseEntity.ok().body(chatService.getAllChatMessages(users.getUserId(),name.trim()));
    }

    @GetMapping("/find-chat")
    public ResponseEntity<?> getChatByDoubleUserId(@AuthenticationPrincipal Users users,
                                                     @RequestParam("userId1") Integer userId1,
                                                   @RequestParam("userId2") Integer userId2) {
        if (users == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("người dùng không tồn tại");
        }

        return ResponseEntity.ok().body(chatService.findByDoubleUserId(userId1,userId2));
    }

    /**
     * doi ten nhom
     * @param users
     * @return /api/chat/change-chat-name
     */
    @PostMapping("/change-chat-name")
    public ResponseEntity<?> changeChatName (@AuthenticationPrincipal Users users,
                                             @Valid @RequestBody UpdateChatName updateChatName
                                             ) {
        if (updateChatName.getChatId() == null || updateChatName.getChatId() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("dữ liệu không hợp lệ");
        } else if (chatService.findChatByChatName(updateChatName.getChatName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đoạn chat này đã tồn tại");
        }
        chatService.updateChatNameByChatId(updateChatName.getChatId(),updateChatName.getChatName());
        return  ResponseEntity.ok().build();
    }

    /**
     * doi anh dai dien
     * @param users
     * @param chatId
     * @param fileNameOld
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(value="/update-group-avatar",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateGroupAvatar (@AuthenticationPrincipal Users users,
                                                @RequestParam("chatId") Integer chatId,
                                                @RequestParam("fileNameOld") String fileNameOld,
                                                @RequestParam("file") MultipartFile file) throws IOException {
        if (chatId == null || chatId < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("dữ liệu không hợp lệ");
        }
        if (file == null || file.isEmpty()) {
            return ResponseEntity.ok("Không có file ảnh");
        }
        if (file.getSize() > 1024*1024) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("kích thước anh quá lớn");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Không đúng định dạng ảnh");
        }
        if (fileNameOld != null && fileNameOld.contains("image/")) {
            fileNameOld = fileNameOld.substring(fileNameOld.indexOf("image/") + "image/".length());
        }
        String fileName = utils.storeFile(file,fileNameOld);
        chatService.updateAvatarChat(chatId,fileName);
        return ResponseEntity.ok("http://localhost:8080/api/user/image/"+fileName);
    }

    @GetMapping("/user-in-list")
    public ResponseEntity<?> userInList (@AuthenticationPrincipal Users users,
                                         @RequestParam("chatId") Integer chatId) {
        if (chatId == null || chatId < 0) {
            return ResponseEntity.badRequest().body("dữ liệu không hợp lệ");
        }
        return ResponseEntity.status(HttpStatus.OK).body(chatService.userListInChat(chatId));
    }



}
