package com.example.demo.config.seeder;

import com.example.demo.entity.Course;
import com.example.demo.entity.Student;
import com.example.demo.entity.Tutor;
import com.example.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class CourseSeeder {

    @Autowired
    private CourseRepository courseRepository;

    public Course createCourse(Tutor tutor, Student student, String subject,
                               int totalSessions, double totalPrice, String timeOfTheLesson,
                               LocalDateTime startTime, LocalDateTime endTime,
                               LocalDate startDate, LocalDate endDate, String status, String notes) {

        Course course = new Course();
        course.setTutor(tutor);
        course.setStudent(student);
        course.setSubject(subject);
        course.setTotalSessions(totalSessions);
        course.setTotalPrice(totalPrice);
        course.setTimeOfTheLesson(timeOfTheLesson);
        course.setStartTime(startTime);
        course.setEndTime(endTime);
        course.setStartDate(startDate);
        course.setEndDate(endDate);
        course.setStatus(status);
        course.setNotes(notes);

        courseRepository.save(course);
        return course;
    }
}
