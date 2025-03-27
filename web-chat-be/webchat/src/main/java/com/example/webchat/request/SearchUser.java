package com.example.webchat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchUser {
    @JsonProperty(namespace = "user_id")
    private Integer userId;

    @JsonProperty(namespace = "full_name")
    private String fullName;

    @JsonProperty(namespace = "thubnail")
    private String thumbnail;

    @JsonProperty(namespace = "type_user")
    private String typeUser;
}
