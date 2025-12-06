package com.example.login.model;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class Course {


    private int id;
    private Integer tutor_Id;
    private Integer student_Id;
    private String subject;
    private int totalSessions;
    private double totalPrice;
    private String timeOfTheLesson;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate createdAt;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String notes;
public  Course( String subject, String notes, String timeOfTheLesson, double totalPrice, int totalSessions)
{
    this.subject=subject;
    this.notes=notes;
    this.timeOfTheLesson=timeOfTheLesson;
    this.totalPrice=totalPrice;
    this.totalSessions=totalSessions;
}
    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public int getTotalSessions() { return totalSessions; }
    public void setTotalSessions(int totalSessions) { this.totalSessions = totalSessions; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getTimeOfTheLesson() { return timeOfTheLesson; }
    public void setTimeOfTheLesson(String timeOfTheLesson) { this.timeOfTheLesson = timeOfTheLesson; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Integer getTutor_Id() {
        return tutor_Id;
    }
    public void setTutor_Id(Integer tutor_Id) {
        this.tutor_Id = tutor_Id;
    }
    public Integer getStudent_Id() {
        return student_Id;
    }
    public void setStudent_Id(Integer student_Id) {
        this.student_Id = student_Id;
    }
}
