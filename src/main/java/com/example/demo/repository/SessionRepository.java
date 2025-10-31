package com.example.demo.repository;

import com.example.demo.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    List<Session> findByCourseId(Integer courseId);
}
