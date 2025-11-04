package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student extends BaseEntity {

    // // Khóa ngoại liên kết đến bảng users
    // @ManyToOne
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    // Trả thêm userId để hiển thị trong JSON
    @Column(name = "user_id", insertable = false, updatable = false)
    private Integer user_Id;

    @Column(nullable = false, length = 100)
    private String fullName;

    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private String grade;

    @Column(columnDefinition = "TEXT")
    private String description;
}
