package com.example.demo.controller;
import com.example.demo.entity.Tutor;
import com.example.demo.repository.TutorRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.example.demo.entity.Course;
import com.example.demo.entity.entity_design.*;
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

    // @GetMapping("/{id}")
    // public ResponseEntity<Course> getCourseById(@PathVariable int id) {
    //     Optional<Course> courseOpt = courseRepository.findById(id);
    //     return courseOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    // }


@GetMapping("/{id}")
public Course getCourseById(@PathVariable("id") int id) {
    Optional<Course> courseOpt = courseRepository.findById(id);
    if (courseOpt.isEmpty()) return null;
    return courseOpt.get();
}


// Lấy courseId bằng sessionId
@GetMapping("/courses_id/{session_id}")
public ResponseEntity<?> getCourseIdBySessionId(@PathVariable int session_id) {
   if(session_id<=0)
   {
    return ResponseEntity.status(404).body(Map.of(
            "error", "SessionId không hợp lệ " +session_id
        ));
   }

   int course_id=0;
   course_id=courseRepository.findCourseIdBySessionId(session_id);


  if (course_id<=0) {
        return ResponseEntity.status(404).body(Map.of(
            "error", "Không tìm thấy tutor cho user: " 
        ));
    }
   return ResponseEntity.ok(course_id);
}


// Lấy courseId bằng sessionId
@GetMapping("/userIdTutorByCourse/{courseId}")
public ResponseEntity<?> getUserIdByCourseId(@PathVariable int courseId) {
   if(courseId<=0)
   {
    return ResponseEntity.status(404).body(Map.of(
            "error", "SessionId không hợp lệ " +courseId
        ));
   }

   int user_id=0;
   user_id=courseRepository.findUserIdByCourseId(courseId);
//    findCourseIdBySessionId(courseId);


  if (user_id<=0) {
        return ResponseEntity.status(404).body(Map.of(
            "error", "Không tìm thấy user: " 
        ));
    }
   return ResponseEntity.ok(user_id);
}

    // @GET("userIdTutorByCourse/{courseId}")
    // Call<Integer> getUserIdTutorByCourse(@Path("courseId") int courseId);


// findUserIdByCourseId(@Param("courseId") int courseId);



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
    List<String> validStatuses = List.of("NEW", "STUDENT_REGISTERED", "ONGOING", "COMPLETED", "CANCELLED");
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


@GetMapping("/my_courses_student/{status}")
public ResponseEntity<?> getMyCoursesStudentByStatus(@PathVariable String status) {
    // 1. Lấy username từ Token
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    
    // 2. Tìm Student (Cẩn thận lỗi Zero Date ở đây nếu chưa fix config DB)
    Optional<Student> studentOpt = studentRepository.findByUser_Username(username);

    if (studentOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
            "error", "Không tìm thấy thông tin học viên cho tài khoản: " + username
        ));
    }

    // 3. Chuẩn hóa input
    String normalizedStatus = status.trim().toUpperCase();
    
    // Danh sách này PHẢI KHỚP CHÍNH XÁC với dữ liệu trong cột 'status' của bảng 'courses'
    List<String> validStatuses = List.of("STUDENT_REGISTERED", "ONGOING", "COMPLETED", "CANCELLED");
    
    if (!validStatuses.contains(normalizedStatus)) {
        return ResponseEntity.badRequest().body(Map.of(
            "error", "Trạng thái '" + status + "' không hợp lệ!",
            "valid_statuses", validStatuses
        ));
    }

    // 4. Gọi Repository (Hàm native query đã fix ở bước trước)
    List<CourseInfo> courseInfo = courseRepository.findCoursesByStudentIdAndStatus(
        studentOpt.get().getId(), normalizedStatus
    );
    
    return ResponseEntity.ok(courseInfo);
}

//lấy chi tiết khoá học, tutor lấy

@GetMapping("/by_tutor/{id}")
public ResponseEntity<?> getCoursesByTutorAndIdCourse(@PathVariable int id) {
    int courseId = id; // Biến id từ URL được dùng làm Course ID
    
    // 4. Gọi Repository với tên hàm đã chuẩn hóa và kiểu trả về là đối tượng đơn
    DetailCourse course = courseRepository.findDetailCourseById(courseId);
    
    // Kiểm tra kết quả
    if (course == null) {
        return ResponseEntity.notFound().build();
    }
    
    return ResponseEntity.ok(course);
}

//lấy thông tin khoá học/ student lấy
@GetMapping("/by_student/{id}")
public ResponseEntity<?> getCoursesByStudetnAndIdCourse(@PathVariable int id) {
    int courseId = id; // Biến id từ URL được dùng làm Course ID
    
    // 4. Gọi Repository với tên hàm đã chuẩn hóa và kiểu trả về là đối tượng đơn
    DetailCourse course = courseRepository.findDetailCourseByIdOfStudent(courseId);
    
    // Kiểm tra kết quả
    if (course == null) {
        return ResponseEntity.notFound().build();
    }
    
    return ResponseEntity.ok(course);

    
}



//  List<Course> findByStatus(String status);


//lấy thông tin khoá học/ student lấy
@GetMapping("/by_all_admin/{status}")
public ResponseEntity<?> getCoursesByStatus(@PathVariable String status) {
    String st=status; // Biến id từ URL được dùng làm Course ID
    
    // 4. Gọi Repository với tên hàm đã chuẩn hóa và kiểu trả về là đối tượng đơn
    List<CourseInfo> course = courseRepository.findCoursesByAllAdminIdAndStatus(st);
    // findCoursesByAdminIdAndStatus
    
    // Kiểm tra kết quả
    if (course == null) {
        return ResponseEntity.notFound().build();
    }
    
    return ResponseEntity.ok(course);    
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
            course.setStatus("NEW");
        
            Course newCourse = courseRepository.save(course);
            int courseId=newCourse.getId();
            //không gửi giữ liệu
            return ResponseEntity.ok(courseId);
        }



    //   @POST("courses/update")
    // Call<CourseInfo> UpdateCourse(@Body Course course);



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
        course.setNotes(updatedCourse.getNotes());
        courseRepository.save(course);
        return ResponseEntity.ok(course);
    }

// ✅ Cập nhật trạng thái khóa học
       

    @PutMapping("/updateCourseStatus/{courseId}")
public ResponseEntity<?> updateCourseStatus(
        @PathVariable("courseId") int courseId,
        @RequestBody String status) {

    Optional<Course> courseOpt = courseRepository.findById(courseId);
    if (courseOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    Course course = courseOpt.get();
    // String normalizedStatus = status.trim().toUpperCase();
    course.setStatus(status.replace("\"", "").trim());

    // course.setStatus(normalizedStatus);
    courseRepository.save(course);

    return ResponseEntity.ok(course);
}


// updateCourseStatus(courseId, status);


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
    List<CourseWithTutorDetail> availableCourses = courseRepository.findAllAvailableCoursesWithTutorDetails();
    if(availableCourses.isEmpty()) {
        return ResponseEntity.ok(Map.of(
            "message", "Hiện không có khóa học nào đang chờ đăng ký."
        ));
    }
    return ResponseEntity.ok(availableCourses);
}


@GetMapping("/count_courses_by_status/{status}")
public ResponseEntity<?> CountCourseByStatus(@PathVariable String status) {
    String st=status;
    int count = courseRepository. countCoursesByStatus(st);
    return ResponseEntity.ok(count);
}
//  countCoursesByStatus(@Param("status") String status);


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


// @PutMapping("/updateCourseStatus/{id}")
// public ResponseEntity<?> updateCourseStatus(@PathVariable int id,@RequestBody String status ) {
//         Optional<Course> courseOpt = courseRepository.findById(id);
//         if (courseOpt.isEmpty()) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                     .body(Map.of("error", "Không tìm thấy khóa học với ID: " +
//                             id));
//         }
//         Course course = courseOpt.get();
//         course.setStatus(status);
//         Course this_course =courseRepository.save(course);
//         return ResponseEntity.ok(this_course);
//     }



    @GetMapping("/by_tutor_id/{tutor_id}")
    public ResponseEntity<?> getCoursesByTutorId(@PathVariable int tutor_id) {
        List<Course> courses = courseRepository.findByTutor_Id(tutor_id);
        if (courses.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "message", "Không có khóa học nào cho tutor ID: " + tutor_id
            ));
        }
        return ResponseEntity.ok(courses);
    }
}
