package com.example.demo.entity.entity_design;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CourseWithTutorDetail {
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer tutor_Id;
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
    private Integer user_Id;
    private String fullName;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private String bio;
    private Integer experienceYears;
    private Double hourlyRate;
    private Boolean verified;
    private Integer tutorTotalSessions;
    private Double averageRating;
    private String profileImage;
}
