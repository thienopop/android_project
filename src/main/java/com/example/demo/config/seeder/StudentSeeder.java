package com.example.demo.config.seeder;

import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class StudentSeeder {

    @Autowired
    private StudentRepository studentRepository;

    public void createStudent(User user, String fullName, String phone, String address,
                              LocalDate dateOfBirth, String grade, String description) {

        Student student = new Student();
        student.setUser(user);
        student.setFullName(fullName);
        student.setPhone(phone);
        student.setAddress(address);
        student.setDateOfBirth(dateOfBirth);
        student.setGrade(grade);
        student.setDescription(description);
        student.setUpdatedAt(LocalDateTime.now());

        studentRepository.save(student);
    }
}
