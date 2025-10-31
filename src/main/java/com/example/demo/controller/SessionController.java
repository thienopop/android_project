package com.example.demo.controller;



import com.example.demo.entity.Session;
import com.example.demo.repository.SessionRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.entity.Course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private CourseRepository courseRepository;

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
}



// timeOfTheLesson
// {
//   "courseId": 1,
//   "sessionDate": "2025-11-02T09:00:00",
//   "duration": 90,
//   "notes": "Buổi học Java cơ bản"
// }


