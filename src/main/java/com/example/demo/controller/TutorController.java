package com.example.demo.controller;

import com.example.demo.entity.Tutor;
import com.example.demo.entity.User;
import com.example.demo.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.config.JwtTokenUtil;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tutors")
public class TutorController {

    @Autowired
    private TutorRepository tutorRepository;

    // Lấy tất cả tutor
    @GetMapping
    public List<Tutor> getAllTutors() {
        return tutorRepository.findAll();
    }

    // Lấy tutor theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Tutor> getTutorById(@PathVariable int id) {
        Optional<Tutor> tutorOpt = tutorRepository.findById(id);
        return tutorOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

 @GetMapping("/me")
public ResponseEntity<?> getMyTutorProfile() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    System.out.println(">>> Current username: " + username);

    Optional<Tutor> tutorOpt = tutorRepository.findByUser_Username(username);
    if (tutorOpt.isEmpty()) {
        return ResponseEntity.status(404).body("Không tìm thấy tutor cho user: " + username);
    }

    return ResponseEntity.ok(tutorOpt.get());
}


    // Thêm tutor mới (chỉ admin)
    @PostMapping
    public ResponseEntity<Tutor> createTutor(@RequestBody Tutor tutor) {
        Tutor savedTutor = tutorRepository.save(tutor);
        return ResponseEntity.ok(savedTutor);
    }

    // Cập nhật tutor (chỉ admin)
    @PutMapping("/{id}")
    public ResponseEntity<Tutor> updateTutor(@PathVariable int id, @RequestBody Tutor updatedTutor) {
        Optional<Tutor> tutorOpt = tutorRepository.findById(id);
        if (tutorOpt.isEmpty()) return ResponseEntity.notFound().build();

        Tutor tutor = tutorOpt.get();
        tutor.setFullName(updatedTutor.getFullName());
        tutor.setPhone(updatedTutor.getPhone());
        tutor.setAddress(updatedTutor.getAddress());
        tutor.setBio(updatedTutor.getBio());
        tutor.setExperienceYears(updatedTutor.getExperienceYears());
        tutor.setHourlyRate(updatedTutor.getHourlyRate());
        tutor.setVerified(updatedTutor.getVerified());
        tutor.setProfileImage(updatedTutor.getProfileImage());

        tutorRepository.save(tutor);
        return ResponseEntity.ok(tutor);
    }
}
