package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ✅ Nên dùng @ManyToOne thay vì @OneToOne (vì một Course có thể có nhiều Session)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;

    // ✅ Trả thêm course_id ra JSON (không ảnh hưởng DB)
    @Column(name = "course_id", insertable = false, updatable = false)
    private Integer courseId;

    @Column(name = "session_date", nullable = false)
    private LocalDateTime sessionDate;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    // ✅ end_time do DB sinh tự động
    @Column(name = "end_time", insertable = false, updatable = false)
    private LocalDateTime endTime;

    @Column(name = "status", nullable = false)
    private String status = "SCHEDULED";

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    // ---- Getters & Setters ----

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public LocalDateTime getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDateTime sessionDate) {
        this.sessionDate = sessionDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt= LocalDateTime.now();
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt= LocalDateTime.now();
    }

// Tính toán endTime dựa trên sessionDate và duration
       public void calculateEndTime() {
        if (sessionDate != null && duration != null) {
            this.endTime = sessionDate.plusMinutes(duration);
        }
    }
}

