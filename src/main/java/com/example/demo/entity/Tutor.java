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
@Table(name = "tutors")
public class Tutor extends BaseEntity {

    // // Liên kết với bảng users (một user có thể là tutor)
    // @OneToOne
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    // Trả thêm userId để hiển thị trong JSON
    @Column(name = "user_id", insertable = false, updatable = false)
    private Integer user_Id;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "experience_years")
    private Integer experienceYears = 0;

    @Column(name = "hourly_rate")
    private Double hourlyRate = 0.0;

    @Column(nullable = false)
    private Boolean verified = false;

    @Column(name = "total_sessions")
    private Integer totalSessions = 0;

    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    @Column(name = "profile_image", length = 255)
    private String profileImage;
}
   