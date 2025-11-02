package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    //  Optional<Student> findFirstByUser_Role(String role);
    //  Optional<Student> findByUser_UsernameAndUser_Role(String username, String role);
    // Optional<User> findFirstByRole(String role);
    Optional<User> findFirstByRole(String role);
    
}
