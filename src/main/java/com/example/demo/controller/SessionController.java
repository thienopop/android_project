package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.demo.entity.Session;
import com.example.demo.entity.Student;
import com.example.demo.entity.Tutor;
import com.example.demo.repository.SessionRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.TutorRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.entity.Course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private StudentRepository studentRepository;

    // 🟢 1. Lấy tất cả buổi học
    @GetMapping
    public ResponseEntity<List<Session>> getAllSessions() {
        List<Session> sessions = sessionRepository.findAll();
        return ResponseEntity.ok(sessions);
    }
    
    // 🟢 2. Lấy buổi học theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSessionById(@PathVariable Integer id) {
        Optional<Session> sessionOpt = sessionRepository.findById(id);
        if (sessionOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Không tìm thấy buổi học với ID = " + id);
        }
        return ResponseEntity.ok(sessionOpt.get());
    }

    // 🟡 3. Tạo buổi học mới
    @PostMapping("/create")
    public ResponseEntity<?> createSession(@RequestBody Session session) {
        if (session.getCourseId() == null) {
            return ResponseEntity.badRequest().body("Thiếu course_id!");
        }

        Optional<Course> courseOpt = courseRepository.findById(session.getCourseId());
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Không tìm thấy khóa học có ID = " + session.getCourseId());
        }

        session.setCourse(courseOpt.get());
        session.calculateEndTime(); // Tính
        
        Session newSession = sessionRepository.save(session);
        return ResponseEntity.ok(newSession);
    }

    // 🟠 4. Cập nhật buổi học
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSession(@PathVariable Integer id, @RequestBody Session updatedSession) {
        Optional<Session> existingOpt = sessionRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Không tìm thấy buổi học ID = " + id);
        }

        Session existing = existingOpt.get();

        // Chỉ cập nhật những trường cho phép
        existing.setSessionDate(updatedSession.getSessionDate());
        existing.setDuration(updatedSession.getDuration());
        existing.setNotes(updatedSession.getNotes());
        existing.setStatus(updatedSession.getStatus());
        existing.calculateEndTime(); // Tính lại endTime nếu cần

        sessionRepository.save(existing);
        return ResponseEntity.ok(existing);
    }

    // 🔴 5. Xóa buổi học
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSession(@PathVariable Integer id) {
        Optional<Session> sessionOpt = sessionRepository.findById(id);
        if (sessionOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Không tìm thấy buổi học ID = " + id);
        }

        sessionRepository.deleteById(id);
        return ResponseEntity.ok("Đã xóa buổi học ID = " + id);
    }

    // 🔵 6. Lấy danh sách buổi học theo khóa học
    @GetMapping("/by-course/{courseId}")
    public ResponseEntity<?> getSessionsByCourse(@PathVariable Integer courseId) {
        List<Session> sessions = sessionRepository.findByCourseId(courseId);
        if (sessions.isEmpty()) {
            return ResponseEntity.status(404).body("Không có buổi học nào cho course_id = " + courseId);
        }
        return ResponseEntity.ok(sessions);
    }





// public List<Session> getSessionsByStudentAndDate(Integer studentId, LocalDate date) {
//     LocalDateTime startOfDay = date.atStartOfDay();
//     LocalDateTime endOfDay = date.atTime(23, 59, 59);

//     return sessionRepository.findSessionsByStudentIdAndDate(studentId, startOfDay, endOfDay);
// }



    // lấy buổi học của student đăng nhập , và n diễ ra gày buổi học
  @GetMapping("/by-student/date/{sessionDate}")
public ResponseEntity<?> getSessionsByStudentStatusDate(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sessionDate) {
  LocalDateTime startOfDay = sessionDate.atStartOfDay();
    LocalDateTime endOfDay = sessionDate.atTime(23, 59, 59);
    // 🔐 Lấy username từ token
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    System.out.println(">>> Current username: " + username);

    // 🔍 Tìm student theo username
    Optional<Student> studentOpt = studentRepository.findByUser_Username(username);
    if (studentOpt.isEmpty()) {
        return ResponseEntity.status(404).body("Không tìm thấy student cho user: " + username);
    }
    Integer studentId = studentOpt.get().getId();

    // // ✅ Tạo khoảng thời gian đầu-ngày, cuối-ngày
    // LocalDateTime startOfDay = sessionDate.atStartOfDay();
    // LocalDateTime endOfDay = sessionDate.atTime(LocalTime.MAX);

    // 🔍 Truy vấn buổi học trong ngày
    List<Session> sessions = sessionRepository.findSessionsByStudentIdAndDate(
            studentId, startOfDay, endOfDay);

    if (sessions.isEmpty()) {
        return ResponseEntity.status(404).body("Không có buổi học nào cho studentId = " + studentId + ", ngày = " + sessionDate);
    }

    return ResponseEntity.ok(sessions);
}





    // lấy buổi học của tutor đăng nhập , và n diễ ra gày buổi học
 // Lấy buổi học của tutor đăng nhập và diễn ra trong ngày chỉ định
@GetMapping("/by-tutor/date/{sessionDate}")
public ResponseEntity<?> getSessionsByTutorAndDate(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sessionDate) {

    // ✅ Tạo khoảng thời gian trong ngày (00:00:00 → 23:59:59)
    LocalDateTime startOfDay = sessionDate.atStartOfDay();
    LocalDateTime endOfDay = sessionDate.atTime(23, 59, 59);

    // 🔐 Lấy username từ token (đã xác thực qua Spring Security)
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    System.out.println(">>> Current username: " + username);

    // 🔍 Tìm tutor theo username
    Optional<Tutor> tutorOpt = tutorRepository.findByUser_Username(username);
    if (tutorOpt.isEmpty()) {
        return ResponseEntity.status(404).body("Không tìm thấy tutor cho user: " + username);
    }
    Integer tutorId = tutorOpt.get().getId();

    // 🔍 Truy vấn buổi học trong ngày
    List<Session> sessions = sessionRepository.findSessionsByTutorIdAndDate(
            tutorId, startOfDay, endOfDay);

    if (sessions.isEmpty()) {
        return ResponseEntity.status(404).body("Không có buổi học nào cho tutorId = " 
                + tutorId + ", ngày = " + sessionDate);
    }

    return ResponseEntity.ok(sessions);
}



}



// timeOfTheLesson
// {
//   "courseId": 1,
//   "sessionDate": "2025-11-02T09:00:00",
//   "duration": 90,
//   "notes": "Buổi học Java cơ bản"
// }


