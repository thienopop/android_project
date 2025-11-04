package com.example.demo.repository;
import com.example.demo.entity.entity_design.SessionInfo;
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


//     // lấy danh sách session của học viên theo ngày
// @Query("SELECT s FROM Session s " +
//        "JOIN s.course c " +
//        "JOIN c.student st " +
//        "WHERE st.id = :studentId " +
//        "AND s.sessionDate BETWEEN :startOfDay AND :endOfDay")
// List<Session> findSessionsByStudentIdAndDate(
//         @Param("studentId") Integer studentId,
//         @Param("startOfDay") LocalDateTime startOfDay,
//         @Param("endOfDay") LocalDateTime endOfDay
// );

//lấy danh sách session của học viên theo trạng thái

// List<Session> findByCourseStudentIdAndStatus(Integer studentId, String status);
// có thể thay thế, cần xem xét lại
// lấy danh sách session của học viên theo trạng thái


@Query("SELECT s FROM Session s " +
       "JOIN s.course c " +
       "JOIN c.student st " +
       "WHERE st.id = :studentId " +
       "AND s.status = :status")
List<Session> findSessionsByStudentIdAndStatus(
        @Param("studentId") Integer studentId,
        @Param("status") String status
);


// lấy danh sách session của gia sư theo trạng thái
@Query("SELECT s FROM Session s " +
       "JOIN s.course c " +
       "JOIN c.tutor tt " +
       "WHERE tt.id = :tutorId " +
       "AND s.status = :status")
List<Session> findSessionsByTutorIdAndStatus(
        @Param("tutorId") Integer tutorId,
        @Param("status") String status
);



// // lấy danh sách session của gia sư theo ngày
// @Query("SELECT s FROM Session s JOIN s.course c JOIN c.tutor t " +
//        "WHERE t.id = :tutorId AND s.sessionDate BETWEEN :startOfDay AND :endOfDay")
// List<Session> findSessionsByTutorIdAndDate(
//         @Param("tutorId") Integer tutorId,
//         @Param("startOfDay") LocalDateTime startOfDay,
//         @Param("endOfDay") LocalDateTime endOfDay);

// //tìm tất cả session của course theo id course
//         List<Session> findByCourse_Id(Integer courseId);



//lấy danh sách session của gia sư theo ngày với thông tin chi tiết
        @Query("SELECT s.id AS id, s.sessionDate AS sessionDate, s.duration AS duration, " +
       "s.notes AS notes, s.status AS status, t.fullName AS fullName, c.subject AS subject " +
       "FROM Session s JOIN s.course c JOIN c.student t " +
       "WHERE c.tutor.id = :tutorId AND s.sessionDate BETWEEN :startOfDay AND :endOfDay")
List<SessionInfo> findSessionsByTutorIdAndDate(
        @Param("tutorId") Integer tutorId,
        @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay);





        //lấy danh sách session của gia sư theo ngày với thông tin chi tiết
       @Query("SELECT s.id AS id, s.sessionDate AS sessionDate, s.duration AS duration, " +
       "s.notes AS notes, s.status AS status, t.fullName AS fullName, c.subject AS subject " +
       "FROM Session s JOIN s.course c JOIN c.tutor t " +
       "WHERE c.student.id = :studentId AND s.sessionDate BETWEEN :startOfDay AND :endOfDay")
List<SessionInfo> findSessionsByStudentIdAndDate(
        @Param("studentId") Integer studentId,
        @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay);



}

  