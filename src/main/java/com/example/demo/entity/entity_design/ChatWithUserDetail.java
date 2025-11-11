package com.example.demo.entity.entity_design;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatWithUserDetail {
    private Integer chatId;
    private Integer userId;
    private String username;
    private String email;
    private String role;
    private String fullName;
    private Boolean hasUnreadMessages;
}
