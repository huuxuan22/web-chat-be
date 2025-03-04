package com.example.webchat.respone;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiRespone {
    private int status;
    private String message;
}
