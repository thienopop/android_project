package com.example.login.model;

public class CourseInfo {


    private int id;
    private int totalSessions;
    private String fullName;
    private String subject;
    private String status;
    private String notes;
    private String startTime;


    public CourseInfo(int id, int totalSessions, String fullName, String subject, String status, String notes, String startTime) {
        this.id = id;
        this.totalSessions= totalSessions;
        this.fullName = fullName;
        this.subject = subject;
        this.status = status;
        this.notes = notes;
        this.startTime = startTime;
    }

    // Getter
    public int getId() { return id; }
    public int getTotalSessions() { return totalSessions; }
    public String getFullName() { return fullName; }
    public String getSubject() { return subject; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
    public String getStartTime() { return startTime; }
}
