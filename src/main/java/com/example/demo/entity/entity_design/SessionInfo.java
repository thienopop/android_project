package com.example.demo.entity.entity_design;

import java.time.LocalDateTime;

public interface SessionInfo {
    Integer getId();
    LocalDateTime getSessionDate();
    Integer getDuration();
    String getNotes();
    String getStatus();
    String getFullName();
    String getSubject();
}
