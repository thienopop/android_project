package com.example.demo.controller;

import com.example.demo.entity.Course;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.Student;
import com.example.demo.entity.Tutor;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedbacks")
public class FeedbackController {
    private final FeedbackRepository feedbackRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createFeedback(@RequestBody Feedback feedback) {
        Optional<Student> studentOpt = studentRepository.findById(feedback.getStudentId());
        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy thông tin học viên"));
        }

        Optional<Course> courseOpt = courseRepository.findById(feedback.getCourseId());
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy khóa học"));
        }

        Course course = courseOpt.get();
        if (course.getStudent().getId() != feedback.getStudentId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Student ID không khớp với khóa học"));
        }

        if (!(course.getStatus().equalsIgnoreCase("COMPLETED"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Chỉ có thể đánh giá khóa học đã hoàn thành"));
        }

        if (feedbackRepository.existsByCourse_Id(feedback.getCourseId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Khóa học này đã được đánh giá"));
        }

        feedback.setStudent(studentOpt.get());
        feedback.setCourse(course);
        Feedback savedFeedback = feedbackRepository.save(feedback);

        updateTutorAverageRating(course.getTutor().getId());

        return ResponseEntity.ok(savedFeedback);
    }

    @GetMapping("/of_course/{courseId}")
    public ResponseEntity<?> getFeedbackByCourseId(@PathVariable int courseId) {
        Optional<Feedback> feedbackOpt = feedbackRepository.findByCourse_Id(courseId);

        if (feedbackOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Chưa có đánh giá cho khóa học này"));
        }

        return ResponseEntity.ok(feedbackOpt.get());
    }

    private void updateTutorAverageRating(int tutorId) {
        List<Feedback> feedbacks = feedbackRepository.findAllByTutorId(tutorId);

        if (feedbacks.isEmpty()) return;

        double averageRating = feedbacks.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);

        Optional<Tutor> tutorOpt = tutorRepository.findById(tutorId);
        if (tutorOpt.isPresent()) {
            Tutor tutor = tutorOpt.get();
            tutor.setAverageRating(Math.round(averageRating * 10.0) / 10.0);
            tutorRepository.save(tutor);
        }
    }
}