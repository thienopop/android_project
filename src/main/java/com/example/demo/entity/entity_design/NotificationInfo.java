package com.example.demo.entity.entity_design;

import java.time.LocalDateTime;

public interface NotificationInfo {

    Integer getUserId();

    String getTitle();

    String getMessage();

    Boolean getIsRead();

    LocalDateTime getCreatedAt();
}
