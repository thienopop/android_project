package com.example.demo.config.seeder;

import com.example.demo.entity.Course;
import com.example.demo.entity.Session;
import com.example.demo.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SessionSeeder {

    @Autowired
    private SessionRepository sessionRepository;

    public void createSession(Course course, LocalDateTime sessionDate, int duration,
                              String status, String notes) {

        Session session = new Session();
        session.setCourse(course);
        session.setSessionDate(sessionDate);
        session.setDuration(duration);
        session.setStatus(status);
        session.setNotes(notes);
        session.calculateEndTime();

        sessionRepository.save(session);
    }
}
