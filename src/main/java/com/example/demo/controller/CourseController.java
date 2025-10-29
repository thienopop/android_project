package com.example.demo.controller;

import com.example.demo.entity.Course;
import com.example.demo.entity.Student;
import com.example.demo.entity.Tutor;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private StudentRepository studentRepository;

    // ✅ Lấy danh sách tất cả khóa học
    @GetMapping
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // ✅ Xem chi tiết 1 khóa học theo id

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable int id) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        return courseOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Tạo mới khóa học (Tutor tạo)
    @PostMapping("/create")
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        // Kiểm tra tutor & student có tồn tại không
         String username = SecurityContextHolder.getContext().getAuthentication().getName();
    // System.out.println(">>> Current username: " + username);

    Optional<Tutor> tutorOpt = tutorRepository.findByUser_Username(username);
    if (tutorOpt.isEmpty()) {
        return ResponseEntity.status(404).body("Không tìm thấy tutor cho user: " + username);
    }
        course.setTutor(tutorOpt.get());
        course.setStatus("PENDING");
        Course saved = courseRepository.save(course);
        return ResponseEntity.ok(saved);

    }

    // ✅ Cập nhật khóa học
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable int id, @RequestBody Course updatedCourse) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isEmpty()) return ResponseEntity.notFound().build();

        Course course = courseOpt.get();
        course.setSubject(updatedCourse.getSubject());
        course.setTotalSessions(updatedCourse.getTotalSessions());
        course.setTotalPrice(updatedCourse.getTotalPrice());
        course.setTimeOfTheLesson(updatedCourse.getTimeOfTheLesson());
        course.setStartTime(updatedCourse.getStartTime());
        course.setEndTime(updatedCourse.getEndTime());
        course.setStartDate(updatedCourse.getStartDate());
        course.setEndDate(updatedCourse.getEndDate());
        course.setStatus(updatedCourse.getStatus());
        course.setNotes(updatedCourse.getNotes());

        courseRepository.save(course);
        return ResponseEntity.ok(course);
    }

    // ✅ Xóa khóa học
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        if (!courseRepository.existsById(id)) return ResponseEntity.notFound().build();
        courseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
