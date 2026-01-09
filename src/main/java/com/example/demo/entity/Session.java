package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sessions")
public class Session extends BaseEntity {

    // Nên dùng @ManyToOne thay vì @OneToOne (vì một Course có thể có nhiều Session)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;

    // Trả thêm course_id ra JSON (không ảnh hưởng DB)
    @Column(name = "course_id", insertable = false, updatable = false)
    private Integer courseId;

    @Column(name = "session_date", nullable = false)
    private LocalDateTime sessionDate;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    // end_time do DB sinh tự động
    @Column(name = "end_time", insertable = false, updatable = false)
    private LocalDateTime endTime;

    @Column(name = "status", nullable = false)
    private String status = "SCHEDULED";

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Tính toán endTime dựa trên sessionDate và duration
    public void calculateEndTime() {
        if (sessionDate != null && duration != null) {
            this.endTime = sessionDate.plusMinutes(duration);
        }
    }
}

