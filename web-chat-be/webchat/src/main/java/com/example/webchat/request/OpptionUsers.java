package com.example.webchat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.JoinColumn;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpptionUsers {
    @JsonProperty(namespace = "user_id")
    private Integer userId;

    @JsonProperty(namespace = "full_name")
    private String fullName;

    @JoinColumn(name = "thubnail")
    private String thumbnail;
}
