package com.example.demo.controller;
import  com.example.demo.entity.entity_design.CourseInfo;
import com.example.demo.entity.Tutor;
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

@RestController
@RequestMapping("/courses")
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





    // chức năng cho tutor


    





    // ✅ Xem chi tiết 1 khóa học theo id/ cả student và tutor đều xem được

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable int id) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        return courseOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

 // lấy danh sách khóa học của tutor hiện tại theo trạng thái
// phải chính tutor đó mới xem được
@GetMapping("/my_courses/{status}")
public ResponseEntity<?> getMyCoursesByStatus(@PathVariable String status) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Optional<Tutor> tutorOpt = tutorRepository.findByUser_Username(username);

    if (tutorOpt.isEmpty()) {
        return ResponseEntity.status(404).body(Map.of(
            "error", "Không tìm thấy tutor cho user: " + username
        ));
    }

    // Chuẩn hóa & kiểm tra status
    status = status.toUpperCase();
    List<String> validStatuses = List.of("PENDING", "STUDENT_REGISTERED", "ONGOING", "COMPLETED", "CANCELLED");
    if (!validStatuses.contains(status)) {
        return ResponseEntity.badRequest().body(Map.of(
            "error", "Trạng thái không hợp lệ!",
            "valid_statuses", validStatuses
        ));
    }

    List<CourseInfo> courseInfo = courseRepository.findCoursesByTutorIdAndStatus(
        tutorOpt.get().getId(), status
    );

    // if (courseInfo.isEmpty()) {
    //     return ResponseEntity.ok(Map.of(
    //         "message", "Không có khóa học nào với trạng thái " + status
    //     ));
    // }

    return ResponseEntity.ok(courseInfo);
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
    @PutMapping("/update")
    public ResponseEntity<?> updateCourse( @RequestBody Course updatedCourse) {
        Optional<Course> courseOpt = courseRepository.findById(updatedCourse.getId());
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
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        if (!courseRepository.existsById(id)) return ResponseEntity.notFound().build();
        courseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    //xác nhận hoàn thành khoá học (tutor xác nhận)

    @PutMapping("/confirm_completion/{id}")
    public ResponseEntity<?> confirmCourseCompletion(@PathVariable int id) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy khóa học với ID: " +
                            id));
        }
        Course course = courseOpt.get();
        course.setStatus("COMPLETED");
        courseRepository.save(course);
        return ResponseEntity.ok(Map.of(
                "message", "Khóa học đã được xác nhận hoàn thành!",
                "courseId", course.getId(),
                "status", course.getStatus()
        ));
    }

    //✅ Hủy khóa học (tutor hủy)
    @PutMapping("/cancel_course/{id}")
    public ResponseEntity<?> cancelCourse(@PathVariable int id) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy khóa học với ID: " +
                            id));
        }
        Course course = courseOpt.get();
        course.setStatus("CANCELLED");
        courseRepository.save(course);
        return ResponseEntity.ok(Map.of(
                "message", "Khóa học đã được hủy!",
                "courseId", course.getId(),
                "status", course.getStatus()
        ));
    }

    //xác nhận bắt đầu khoá học (tutor xác nhận)
    @PutMapping("/confirm_start/{id}")
    public ResponseEntity<?> confirmCourseStart(@PathVariable int id) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy khóa học với ID: " +
                            id));
        }
        Course course = courseOpt.get();
        course.setStatus("ONGOING");
        courseRepository.save(course);
        return ResponseEntity.ok(Map.of(
                "message", "Khóa học đã được xác nhận bắt đầu!",
                "courseId", course.getId(),
                "status", course.getStatus()
        ));
    }





// chức năng cho student









        // ✅ Đăng ký khóa học (Student đăng ký)
@PutMapping("/register_course_by_student/{id}")
public ResponseEntity<?> registerCourse(@PathVariable int id) {
    // 🔹 Tìm khóa học theo ID
    Optional<Course> courseOpt = courseRepository.findById(id);
    if (courseOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Không tìm thấy khóa học với ID: " + id));
    }

    // 🔹 Lấy username của người dùng hiện tại (đã login bằng token)
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    // 🔹 Tìm student tương ứng với username
    Optional<Student> studentOpt = studentRepository.findByUser_UsernameAndUser_Role(username, "STUDENT");
    // Optional<Student> findByUser_UsernameAndUser_Role(String username, String role);

    if (studentOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Không tìm thấy sinh viên cho user: " + username));
    }

    Course course = courseOpt.get();

    // 🔹 Kiểm tra xem khóa học đã có student chưa
    if (course.getStudent() != null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Khóa học này đã có sinh viên đăng ký rồi."));
    }

    // 🔹 Gán sinh viên hiện tại vào khóa học
    course.setStudent(studentOpt.get());
    course.setStatus("STUDENT_REGISTERED"); // hoặc "ONGOING" nếu bạn muốn bắt đầu ngay

    // 🔹 Lưu lại thay đổi
    courseRepository.save(course);

    return ResponseEntity.ok(Map.of(
            "message", "Sinh viên đã đăng ký khóa học thành công!",
            "courseId", course.getId(),
            "studentName", studentOpt.get().getFullName(),
            "status", course.getStatus()
    ));
}

//lấy dnh sách khóa học đang chờ đăng ký cho student
@GetMapping("/available_courses")
public ResponseEntity<?> getAvailableCoursesForStudent() {
    List<Course> availableCourses = courseRepository.findByStatus("PENDING");
    if(availableCourses.isEmpty()) {
        return ResponseEntity.ok(Map.of(
            "message", "Hiện không có khóa học nào đang chờ đăng ký."
        ));
    }
    return ResponseEntity.ok(availableCourses);
}
// sinh viên lấy khoá học của mình
@GetMapping("/my_courses_student/me/{status}")
public ResponseEntity<?> getMyCoursesAsStudent( @PathVariable String status) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Optional<Student> studentOpt = studentRepository.findByUser_UsernameAndUser_Role(username
, "STUDENT");
    if (studentOpt.isEmpty()) {
        return ResponseEntity.status(404).body("Không tìm thấy student cho user: " + username);
    }
    List<Course> courses = courseRepository.findByStudentAndStatus(studentOpt.get(), status);
    if(courses.isEmpty()) {
        return ResponseEntity.ok(Map.of(
            "message", "Không có khóa học nào."
        ));
    }
    return ResponseEntity.ok(courses);

}
// huỷ đăng ký khoá học (student hủy) với điều kiện chưa bắt đầu khoá học
@PutMapping("/cancel_registration_by_student/{id}")
public ResponseEntity<?> cancelCourseRegistrationByStudent(@PathVariable int id) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy khóa học với ID: " +
                            id));
        }
        Course course = courseOpt.get();
        if(course.getStatus() != "STUDENT_REGISTERED") {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "không thể huỷ hoá học"));
        }
        course.setStatus("PENDING");
        Course this_course =courseRepository.save(course);
        return ResponseEntity.ok(this_course);
    }
}
