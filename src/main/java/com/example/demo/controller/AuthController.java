package com.example.demo.controller;

import com.example.demo.config.JwtTokenUtil;
import com.example.demo.entity.User;
import com.example.demo.entity.Tutor;
import com.example.demo.entity.Student;


import com.example.demo.repository.UserRepository;
import com.example.demo.repository.TutorRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private TutorRepository tutorRepository;
    private StudentRepository studentRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Hệ thống xác thực đang hoạt động");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username).orElse(null);

        if (currentUser == null) {
            return ResponseEntity.status(404).body("Không tìm thấy người dùng cho user: " + username);
        }

        return ResponseEntity.ok(currentUser);
    }



 // 1. Ensures User and Tutor/Student are saved together, or not at all
@PostMapping("/register")
public ResponseEntity<?> registerUser(@RequestBody User user) {

    // 2. Check Username (Return JSON)
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", "Username đã được sử dụng!"));
    }

    // 3. Check Email (Return JSON)
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", "Email đã được sử dụng!"));
    }

    try {
        // 4. Encode Password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 5. Save User
        User savedUser = userRepository.save(user);

        // 6. Save Role Specific Profile
        if ("TUTOR".equals(savedUser.getRole())) {
            Tutor tutor = new Tutor();
            tutor.setUser_Id(savedUser.getId()); // Ensure your Tutor entity has this setter
            tutorRepository.save(tutor);
        } else if ("STUDENT".equals(savedUser.getRole())) {
            Student student = new Student();
            student.setUser_Id(savedUser.getId()); // Ensure your Student entity has this setter
            studentRepository.save(student);
        }

        // 7Generate Token
        String token = jwtTokenUtil.generateToken(savedUser.getUsername());

        // 8. Return Success JSON
        return ResponseEntity.ok(Map.of(
                "message", "Đăng ký thành công",
                "token", token,
                "role", savedUser.getRole() // Optional: useful to send back to Android
        ));

    } catch (Exception e) {
        // Catch unexpected database errors
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Lỗi hệ thống: " + e.getMessage()));
    }
}

@PostMapping("/login")
public ResponseEntity<?> studentLogin(@RequestBody Map<String, String> body) {
    String username = body.get("username");
    String password = body.get("password");

    // Kiểm tra có user hay không
    User user = userRepository.findByUsername(username).orElse(null);
    if (user == null) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Người dùng không tồn tại!");
    }

    // Kiểm tra mật khẩu
    if (!passwordEncoder.matches(password, user.getPassword())) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Mật khẩu không đúng!");
    }
    //  ResponseEntity.status(404).body("Không tìm thấy tutor cho user: " + username);

    // Sinh token
    String token = jwtTokenUtil.generateToken(user.getUsername());

    return ResponseEntity.ok(Map.of(
            "message", "Đăng nhập thành công",
            "token", token,
            "currentUserId", user.getId(),
            "role",user.getRole()
    ));
}



}
