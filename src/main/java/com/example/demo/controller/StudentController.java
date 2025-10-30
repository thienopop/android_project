package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Tutor;
import com.example.demo.entity.User;
import com.example.demo.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.example.demo.entity.Course;
import com.example.demo.entity.Student;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Lấy tất cả hồ sơ học viên
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }



    // ✅ Lấy hồ sơ học viên theo ID
   //lấy thông tin tutor của user hiện tại
// chính tutor đăng nhập để lấy thông tin của mình
 @GetMapping("/me")
public ResponseEntity<?> getMyTutorProfile() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    System.out.println(">>> Current username: " + username);

    Optional<Student> studentOpt = studentRepository.findByUser_UsernameAndUser_Role(username, "STUDENT");
    if (studentOpt.isEmpty()) {
        return ResponseEntity.status(404).body("Không tìm thấy student cho user: " + username);
    }
    return ResponseEntity.ok(studentOpt.get());
}

    // // ✅ Tạo mới hồ sơ học viên
    // @PostMapping
    // public ResponseEntity<?> createStudent(@RequestBody Student student) {
    //     if (student.getUser() == null) {
    //         return ResponseEntity.badRequest().body("Thiếu thông tin user_id");
    //     }
    //     // Kiểm tra user có tồn tại
    //     Optional<User> user = userRepository.findById(student.getUser().getId());
    //     if (user.isEmpty()) {
    //         return ResponseEntity.badRequest().body("User không tồn tại");
    //     }

    //     student.setUser(user.get());
    //     Student saved = studentRepository.save(student);
    //     return ResponseEntity.ok(saved);
    // }

    // ✅ Cập nhật hồ sơ học viên
    @PutMapping("/update_by_student")
    public ResponseEntity<?> updateStudent( @RequestBody Student updated) {
         String username = SecurityContextHolder.getContext().getAuthentication().getName();
    System.out.println(">>> Current username: " + username);

    Optional<Student> studentOpt = studentRepository.findByUser_UsernameAndUser_Role(username, "STUDENT");
        if (studentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = studentOpt.get();
        student.setFullName(updated.getFullName());
        student.setPhone(updated.getPhone());
        student.setAddress(updated.getAddress());
        student.setDateOfBirth(updated.getDateOfBirth());
        student.setGrade(updated.getGrade());
        student.setDescription(updated.getDescription());
        studentRepository.save(student);
        return ResponseEntity.ok(student);
    }

    //lấy hồ sơ học viên theo ID
    @GetMapping("/get_student/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        return studentOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    // ✅ Xóa hồ sơ học viên
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable int id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
