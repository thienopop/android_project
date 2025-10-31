package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Liên kết với Tutor
    // @ManyToOne
    // @JoinColumn(name = "tutor_id", nullable = false)
    // private Tutor tutor;


@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Tutor tutor;

    // Trả thêm userId để hiển thị trong JSON
    @Column(name = "tutor_id", insertable = false, updatable = false)
    private Integer tutor_Id;



    // // Liên kết với Student
    // @ManyToOne
    // @JoinColumn(name = "student_id")
    // private Student student;


@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Student student;

    // Trả thêm userId để hiển thị trong JSON
    @Column(name = "student_id", insertable = false, updatable = false)
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

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Tutor getTutor() { return tutor; }
    public void setTutor(Tutor tutor) { this.tutor = tutor; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

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
