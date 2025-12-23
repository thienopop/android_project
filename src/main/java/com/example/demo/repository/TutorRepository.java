package com.example.demo.repository;

import com.example.demo.entity.Tutor;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Optional<Tutor> findByUser(User user);
    Optional<Tutor> findById(int id);

    Optional<Tutor> findByUser_Username(String username);
    Optional<Tutor> findByUser_Email(String email);
    // Optional<Tutor> findByUser_UsernameAndRole(String username, String role);
    Optional<Tutor> findByUser_UsernameAndUser_Role(String username, String role);
    List<Tutor> findByVerified(boolean verified);

@Query(
    value = "SELECT COUNT(t.id) FROM tutor t WHERE WHERE t.creates_at BETWEEN :startOfDay AND :endOfDay",
    nativeQuery = true)
int countNewTutor( @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay);


}
