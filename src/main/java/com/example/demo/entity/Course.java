package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
public class Course extends BaseEntity {

    // Liên kết với Tutor
    // @ManyToOne
    // @JoinColumn(name = "tutor_id", nullable = false)
    // private Tutor tutor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    @JsonIgnore
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
    @JsonIgnore
    private Student student;

    // Trả thêm userId để hiển thị trong JSON
    @Column(name = "student_id", insertable = false, updatable = false)
    private Integer student_Id;

    private String subject;
    private Integer totalSessions;
    private Double totalPrice;
    private String timeOfTheLesson;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String notes;
}
