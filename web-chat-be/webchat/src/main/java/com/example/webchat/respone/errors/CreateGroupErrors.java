package com.example.webchat.respone.errors;

import com.example.webchat.validation.NotExistUserInList;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateGroupErrors {
    private String userIds;
    private String groupName;
    private String chatImage;
}
