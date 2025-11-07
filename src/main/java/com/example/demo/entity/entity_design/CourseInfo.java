package com.example.demo.entity.entity_design;

import java.time.LocalDateTime;

public interface CourseInfo {
    Long getId();
     LocalDateTime  getStartTime();
    Integer getTotalSessions();
    String getNotes();
    String getStatus();
    String getFullName();
    String getSubject();
    //  private int id;
    // private int totalSessions;
    // private String fullName;
    // private String subject;
    // private String status;
    // private String notes;
    // private String startTime;
}


