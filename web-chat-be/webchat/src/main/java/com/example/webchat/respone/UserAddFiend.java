package com.example.webchat.respone;

import jakarta.persistence.JoinColumn;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddFiend {
    private Long userId;   // Đổi từ Integer -> Long
    private String fullName;
    private String thubnail;
    private String username;
    private Long isGroup;

}
