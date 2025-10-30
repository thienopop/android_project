package com.example.demo.repository;

import com.example.demo.entity.Student;
import com.example.demo.entity.Tutor;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Optional<Tutor> findByUser(User user);
    Optional<Tutor> findById(int id);

    Optional<Tutor> findByUser_Username(String username);
    Optional<Tutor> findByUser_Email(String email);
    // Optional<Tutor> findByUser_UsernameAndRole(String username, String role);
     Optional<Tutor> findByUser_UsernameAndUser_Role(String username, String role);

    
}
