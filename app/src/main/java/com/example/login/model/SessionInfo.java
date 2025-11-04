package com.example.login.model;


public class SessionInfo {
    private int id;
    private int duration;
    private String fullName;
    private String subject;
    private String status;
    private String notes;
    private String sessionDate;

    public SessionInfo(int id, int duration, String fullName, String subject, String status, String notes, String sessionDate) {
        this.id = id;
        this.duration = duration;
        this.fullName = fullName;
        this.subject = subject;
        this.status = status;
        this.notes = notes;
        this.sessionDate = sessionDate;
    }

    // Getter
    public int getId() { return id; }
    public int getDuration() { return duration; }
    public String getFullName() { return fullName; }
    public String getSubject() { return subject; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
    public String getSessionDate() { return sessionDate; }
}
