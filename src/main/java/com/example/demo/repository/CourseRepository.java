package com.example.demo.repository;

import com.example.demo.entity.Course;
import com.example.demo.entity.Tutor;
import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByTutor(Tutor tutor);
    List<Course> findByStudent(Student student);
    List<Course> findByStatus(String status);
    List<Course> findByTutorAndStatus(Tutor tutor, String status);
    List<Course> findByStudentAndStatus(Student student, String status);


}
