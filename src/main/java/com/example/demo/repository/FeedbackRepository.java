package com.example.demo.repository;

import com.example.demo.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    Optional<Feedback> findByCourse_Id(int courseId);

    boolean existsByCourse_Id(int courseId);

    @Query("SELECT f FROM Feedback f WHERE f.course.tutor.id = :tutorId")
    List<Feedback> findAllByTutorId(@Param("tutorId") int tutorId);
}
