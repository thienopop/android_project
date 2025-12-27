package com.example.demo.repository;

import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByUser(User user);
    Optional<Student> findById(int id);
    Optional<Student> findByUser_Username(String username);
    Optional<Student> findByUser_Email(String email);
    Optional<Student> findByUser_UsernameAndUser_Role(String username, String role);
//  Optional<Student> findByUser_UsernameAndUser_Role(String username, String role);

@Query(
    value = "SELECT COUNT(s.id) FROM students s WHERE s.created_at BETWEEN :startOfDay AND :endOfDay",
    nativeQuery = true)
int countNewStudents( @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay);


}

