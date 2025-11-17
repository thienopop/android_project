package com.example.demo.repository;

import com.example.demo.entity.Course;
import com.example.demo.entity.Tutor;
import com.example.demo.entity.entity_design.CourseInfo;
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
    List<Course> findByTutorAndStatus(Tutor tutor, String status);
    List<Course> findByStudentAndStatus(Student student, String status);

    @Query("SELECT c.id AS id, c.startTime AS startTime, c.totalSessions AS totalSessions, c.notes AS notes, c.status AS status, s.fullName AS fullName, c.subject AS subject " +
       "FROM Course c LEFT JOIN Student s ON s.id = c.student.id " +
       "WHERE c.tutor.id = :tutorId AND c.status = :status")
List<CourseInfo> findCoursesByTutorIdAndStatus(@Param("tutorId") int tutorId, @Param("status") String status);



    @Query(value = "SELECT " +
            "    c.id AS id, " +
            "    c.start_time AS startTime, " +
            "    c.total_sessions AS totalSessions, " +
            "    c.notes AS notes, " +
            "    c.status AS status, " +
            "    s.full_name AS fullName, " +
            "    c.subject AS subject, " +
            "    COUNT(ss.id) AS sessionCompleted" + // Lưu ý: bỏ dấu gạch dưới để khớp với getter trong Java
            "FROM " +
            "    courses c " +
            "LEFT JOIN " +
            "    students s ON c.student_id = s.id " +
            "LEFT JOIN " +
            "    sessions ss ON c.id = ss.course_id AND ss.status = 'COMPLETED' " + // Hoặc 'COMPLETED' tùy bạn
            "WHERE " +
            "    c.tutor_id = :tutorId " +
            "GROUP BY " +
            "    c.id, c.start_time, c.total_sessions, c.notes, c.status, s.full_name, c.subject",
            nativeQuery = true)
    List<CourseInfo> findCoursesWithSessionCount(@Param("tutorId") Integer tutorId);
}
