package com.example.demo.controller;

import com.example.demo.entity.Tutor;
import com.example.demo.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tutors")
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
//lấy thông tin tutor của user hiện tại
// chính tutor đăng nhập để lấy thông tin của mình
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
    //  @PostMapping("/createUserTutor")
    // public ResponseEntity<Tutor> createTutorByUser(@RequestBody Tutor tutor) {
    //     Tutor savedTutor = tutorRepository.save(tutor);
    //     return ResponseEntity.ok(savedTutor);
    // }

    // Cập nhật tutor 
    // chính tutor cập nhật thông tin của mình
    @PutMapping("/update")
    public ResponseEntity<Tutor> updateTutor( @RequestBody Tutor updatedTutor) {
        // Optional<Tutor> tutorOpt = tutorRepository.findById(id);

         String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Tutor> tutorOpt = tutorRepository.findByUser_Username(username);
        if (tutorOpt.isEmpty()) return ResponseEntity.notFound().build();
        Tutor tutor = tutorOpt.get();
       if (updatedTutor.getFullName() != null)
       {
            tutor.setFullName(updatedTutor.getFullName());
       }
       if(updatedTutor.getPhone() != null){
        tutor.setPhone(updatedTutor.getPhone());
       }
       if(updatedTutor.getAddress() != null){
        tutor.setAddress(updatedTutor.getAddress());
       }
       if(updatedTutor.getBio() != null){
        tutor.setBio(updatedTutor.getBio());
       }
       if(updatedTutor.getExperienceYears() != null){
         tutor.setExperienceYears(updatedTutor.getExperienceYears());
       }
       if(updatedTutor.getDateOfBirth() != null){
       
        tutor.setDateOfBirth(updatedTutor.getDateOfBirth());
         }
         if(updatedTutor.getHourlyRate() != null){
        tutor.setHourlyRate(updatedTutor.getHourlyRate());
         }
        // cập nhật verified bằng phương pháp khác
        // tutor.setVerified(updatedTutor.getVerified());
        if(updatedTutor.getProfileImage() != null){
        tutor.setProfileImage(updatedTutor.getProfileImage());
    }

        tutorRepository.save(tutor);
        return ResponseEntity.ok(tutor);
    }
}
