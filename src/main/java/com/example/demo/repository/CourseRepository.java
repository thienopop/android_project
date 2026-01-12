package com.example.demo.repository;

import com.example.demo.entity.Course;
import com.example.demo.entity.Session;
import com.example.demo.entity.Tutor;
import com.example.demo.entity.entity_design.*;
import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByTutor(Tutor tutor);
    List<Course> findByStudent(Student student);
    List<Course> findByStatus(String status);
    List<Course> findByTutor_Id(Integer tutorId);
    List<Course> findByTutorAndStatus(Tutor tutor, String status);
    List<Course> findByStudentAndStatus(Student student, String status);
//     Course findById(Integer id);

//     int findCourseIdBySessonId(int sessionId);

//     @Query("SELECT c.id AS id, c.startTime AS startTime, c.totalSessions AS totalSessions, c.notes AS notes, c.status AS status, s.fullName AS fullName, c.subject AS subject " +
//        "FROM Course c LEFT JOIN Student s ON s.id = c.student.id " +
//        "WHERE c.tutor.id = :tutorId AND c.status = :status")
// List<CourseInfo> findCoursesByTutorIdAndStatus(@Param("tutorId") int tutorId, @Param("status") String status);


@Query(value = "SELECT " +
        "    c.id AS id, " +
        "    c.start_time AS startTime, " +
        "    c.total_sessions AS totalSessions, " +
        "    c.notes AS notes, " +
        "    c.status AS status, " +
        "    s.full_name AS fullName, " +
        "    c.subject AS subject, " +
        "    COUNT(ss.id) AS sessionCompleted " + // ADDED SPACE HERE
        "FROM " +
        "    courses c " +
        "LEFT JOIN " +
        "    students s ON c.student_id = s.id " +
        "LEFT JOIN " +
        "    sessions ss ON c.id = ss.course_id AND ss.status = 'COMPLETED' " +
        "WHERE " +
        "    c.tutor_id = :tutorId " +
        "AND c.status = :status " +
        "GROUP BY " +
        "    c.id, c.start_time, c.total_sessions, c.notes, c.status, s.full_name, c.subject",
        nativeQuery = true)
List<CourseInfo> findCoursesByTutorIdAndStatus(@Param("tutorId") Integer tutorId, @Param("status") String status);


@Query(value = "SELECT " +
        "    c.id AS id, " +
        "    c.start_time AS startTime, " +
        "    c.total_sessions AS totalSessions, " +
        "    c.notes AS notes, " +
        "    c.status AS status, " +
        "    t.full_name AS fullName, " + // Lấy tên của Tutor (t)
        "    c.subject AS subject, " +
        "    COUNT(ss.id) AS sessionCompleted " + // ĐÃ THÊM DẤU CÁCH Ở CUỐI DÒNG NÀY
        "FROM " +
        "    courses c " +
        "LEFT JOIN " +
        "    tutors t ON c.tutor_id = t.id " + // Join vào bảng tutors dựa trên tutor_id của khóa học
        "LEFT JOIN " +
        "    sessions ss ON c.id = ss.course_id AND ss.status = 'COMPLETED' " +
        "WHERE " +
        "    c.student_id = :studentId " + // Lọc các khóa học của học viên này
        "AND c.status = :status " +
        "GROUP BY " +
        "    c.id, c.start_time, c.total_sessions, c.notes, c.status, t.full_name, c.subject",
        nativeQuery = true)
List<CourseInfo> findCoursesByStudentIdAndStatus(@Param("studentId") Integer studentId, @Param("status") String status);



@Query(value = "SELECT " +
        "    c.id AS id, " +
        "    c.start_time AS startTime, " +
        "    c.total_sessions AS totalSessions, " +
        "    c.notes AS notes, " +
        "    c.status AS status, " +
        "    t.full_name AS fullName, " + // Lấy tên của Tutor (t)
        "    c.subject AS subject, " +
        "    COUNT(ss.id) AS sessionCompleted " + // ĐÃ THÊM DẤU CÁCH Ở CUỐI DÒNG NÀY
        "FROM " +
        "    courses c " +
        "LEFT JOIN " +
        "    tutors t ON c.tutor_id = t.id " + // Join vào bảng tutors dựa trên tutor_id của khóa học
        "LEFT JOIN " +
        "    sessions ss ON c.id = ss.course_id AND ss.status = 'COMPLETED' " +
        "WHERE " +
        "c.status = :status " +
        "GROUP BY " +
        "    c.id, c.start_time, c.total_sessions, c.notes, c.status, t.full_name, c.subject",
        nativeQuery = true)
List<CourseInfo> findCoursesByAllAdminIdAndStatus(@Param("status") String status);


//Tutor lấy thôgn tin khoá học
@Query(value = "SELECT " +
        "    c.id AS id, " +
        "    c.start_time AS startDate, " +
        "    c.total_sessions AS totalSessions, " +
        "    c.notes AS notes, " +
         "    c.total_price AS totalPrice, " +
           "    c.time_of_the_lesson AS timeOfTheLesson, " +
        "    c.status AS status, " +
        "    s.full_name AS fullName, " +
        "    s.user_id AS userId, " +
        "    c.subject AS subject, " +

        "    COUNT(ss.id) AS completedSessions " +
        "FROM " +
        "    courses c " +
        "LEFT JOIN " +
        "    students s ON c.student_id = s.id " +
        "LEFT JOIN " +
        "    sessions ss ON c.id = ss.course_id AND ss.status = 'COMPLETED' " +
        "WHERE " +
        "    c.id = :courseId " +
        "GROUP BY " +
        "    c.id, c.start_time, c.total_sessions, c.notes, c.status, s.full_name, c.subject",
        nativeQuery = true)
DetailCourse findDetailCourseById(@Param("courseId") int courseId);
// 

//Studetn lấy thông tin khoa học
@Query(value = "SELECT " +
        "    c.id AS id, " +
        "    c.start_time AS startDate, " +
        "    c.total_sessions AS totalSessions, " +
        "    c.notes AS notes, " +
         "    c.total_price AS totalPrice, " +
           "    c.time_of_the_lesson AS timeOfTheLesson, " +
        "    c.status AS status, " +
        "    s.full_name AS fullName, " +
         "    s.user_id AS userId, " +

        "    c.subject AS subject, " +

        "    COUNT(ss.id) AS completedSessions " +
        "FROM " +
        "    courses c " +
        "JOIN " +
        "    tutors s ON c.tutor_id = s.id " +
        "LEFT JOIN " +
        "    sessions ss ON c.id = ss.course_id AND ss.status = 'COMPLETED' " +
        "WHERE " +
        "    c.id = :courseId " +
        "GROUP BY " +
        "    c.id, c.start_time, c.total_sessions, c.notes, c.status, s.full_name, c.subject",
        nativeQuery = true)
DetailCourse findDetailCourseByIdOfStudent(@Param("courseId") int courseId);
// 

@Query(value = """
        SELECT c.id
        FROM courses c
        JOIN sessions s ON c.id = s.course_id
        WHERE s.id = :sessionId
        """,
        nativeQuery = true)
int findCourseIdBySessionId(@Param("sessionId") int sessionId);

    @Query("""
        SELECT new com.example.demo.entity.entity_design.CourseWithTutorDetail(
            c.id,
            c.createdAt,
            c.updatedAt,
            c.tutor_Id,
            c.student_Id,
            c.subject,
            c.totalSessions,
            c.totalPrice,
            c.timeOfTheLesson,
            c.startTime,
            c.endTime,
            c.startDate,
            c.endDate,
            c.status,
            c.notes,
            t.user_Id,
            t.fullName,
            t.phone,
            t.address,
            t.dateOfBirth,
            t.bio,
            t.experienceYears,
            t.hourlyRate,
            t.verified,
            t.totalSessions as tutorTotalSessions,
            t.averageRating,
            t.profileImage
        )
        FROM Course c
        JOIN c.tutor t
        WHERE c.status = 'NEW'
        """)
    List<CourseWithTutorDetail> findAllAvailableCoursesWithTutorDetails();




@Query(
    value = "SELECT COUNT(c.id) FROM courses c WHERE c.status = :status",
    nativeQuery = true
)
int countCoursesByStatus(@Param("status") String status);




@Query(value = """
        SELECT u.id
        FROM courses c
        JOIN tutors t ON c.id = t.course_id 
        JOIN users u ON t.user_id = u.id
        WHERE c.id = :courseId
        """,
        nativeQuery = true)
Integer findUserIdByCourseId(@Param("courseId") int courseId);




}

