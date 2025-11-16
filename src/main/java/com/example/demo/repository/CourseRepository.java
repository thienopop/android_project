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


}
