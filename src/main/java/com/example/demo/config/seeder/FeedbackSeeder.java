package com.example.demo.config.seeder;

import com.example.demo.entity.Course;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.Student;
import com.example.demo.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeedbackSeeder {
    @Autowired
    private FeedbackRepository feedbackRepository;

    public Feedback createFeedback(Course course, Student student, int rating, String comment) {
        Feedback feedback = new Feedback();
        feedback.setCourse(course);
        feedback.setStudent(student);
        feedback.setRating(rating);
        feedback.setComment(comment);

        feedbackRepository.save(feedback);
        return feedback;
    }
}
