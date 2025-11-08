package com.example.demo.config.seeder;

import com.example.demo.entity.Tutor;
import com.example.demo.entity.User;
import com.example.demo.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class TutorSeeder {

    @Autowired
    private TutorRepository tutorRepository;

    public Tutor createTutor(User user, String fullName, String phone, String address,
                             LocalDate dateOfBirth, String bio, Integer experienceYears,
                             Double hourlyRate, Boolean verified, Integer totalSessions,
                             Double averageRating, String profileImage) {

        Tutor tutor = new Tutor();
        tutor.setUser(user);
        tutor.setFullName(fullName);
        tutor.setPhone(phone);
        tutor.setAddress(address);
        tutor.setDateOfBirth(dateOfBirth);
        tutor.setBio(bio);
        tutor.setExperienceYears(experienceYears);
        tutor.setHourlyRate(hourlyRate);
        tutor.setVerified(verified);
        tutor.setTotalSessions(totalSessions);
        tutor.setAverageRating(averageRating);
        tutor.setProfileImage(profileImage);
        tutor.setCreatedAt(LocalDateTime.now());
        tutor.setUpdatedAt(LocalDateTime.now());

        tutorRepository.save(tutor);
        return tutor;
    }
}
