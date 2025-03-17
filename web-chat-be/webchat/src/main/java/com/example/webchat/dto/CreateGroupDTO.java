package com.example.webchat.dto;

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
public class CreateGroupDTO {
    @NotEmpty(message = "Danh sách user không được để trống")
    @NotExistUserInList
    private List<Integer> userIds;

    @NotNull(message = "Tên nhóm không được để trống")
    @Size(min = 3, max = 50, message = "Tên nhóm phải có từ 3 đến 50 ký tự")
    private String groupName;

}
