package com.example.demo.repository;

import com.example.demo.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    List<Session> findByCourseId(Integer courseId);
    List<Session> findByStatus(String status);
    
    // List<Session> findByStudentIdAndSessionDate(Integer studentId, LocalDateTime sessionDate);
    // List<Session> findByTutorIdAndSessionDate(Integer tutorId, LocalDateTime sessionDate);
    // List<Session> findByTutorIdAndSessionDateBetween(Integer tutorId,LocalDateTime startDate, LocalDateTime endDate);
    // List<Session> findByStudentIdAndSessionDateBetween(Integer studentId, LocalDateTime startDate, LocalDateTime endDate);
 
//    List<Session> findSessionsByStudentId(@Param("studentId") Integer studentId);

@Query("SELECT s FROM Session s " +
       "JOIN s.course c " +
       "JOIN c.student st " +
       "WHERE st.id = :studentId " +
       "AND s.sessionDate BETWEEN :startOfDay AND :endOfDay")
List<Session> findSessionsByStudentIdAndDate(
        @Param("studentId") Integer studentId,
        @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay
);


// @Query("SELECT s FROM Session s " +
//        "JOIN s.course c " +
//        "JOIN c.tutor st " +
//        "WHERE st.id = :tutorId " +
//        "AND s.sessionDate BETWEEN :startOfDay AND :endOfDay")
// List<Session> findSessionsByTutorIdAndDate(
//         @Param("studentId") Integer tutorId,
//         @Param("startOfDay") LocalDateTime startOfDay,
//         @Param("endOfDay") LocalDateTime endOfDay
// );



@Query("SELECT s FROM Session s JOIN s.course c JOIN c.tutor t " +
       "WHERE t.id = :tutorId AND s.sessionDate BETWEEN :startOfDay AND :endOfDay")
List<Session> findSessionsByTutorIdAndDate(
        @Param("tutorId") Integer tutorId,
        @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay);


}

  