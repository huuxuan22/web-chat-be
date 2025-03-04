package com.example.webchat.request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupChatReques {
    private List<Integer> userIds; // danh sach nguoi tham gia nhom chat
    private String chatName;
    private String chatImage;

}
