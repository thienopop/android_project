package com.example.demo.controller;

import com.example.demo.config.JwtTokenUtil;
import com.example.demo.entity.User;
// import com.example.demo.entity.Tutor;

import com.example.demo.repository.UserRepository;
import com.example.demo.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private TutorRepository tutorRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

   @PostMapping("/register")
public ResponseEntity<?> registerUser(@RequestBody User user) {
    // Kiểm tra username đã tồn tại chưa
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        return ResponseEntity
                .badRequest()
                .body("Username đã được sử dụng!");
    }

    // Kiểm tra email đã tồn tại chưa
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        return ResponseEntity
                .badRequest()
                .body("Email đã được sử dụng!");
                
            //     (Mapof()
            //         "message", "Email đã được sử dụng!",
            // "token", null
            //    ););
    }
    user.setRole("TUTOR"); // Mặc định role là tutor

    // Mã hoá mật khẩu trước khi lưu
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    // Lưu user mới vào database
    // userRepository.save(user);
    User savedUser = userRepository.save(user);

    //  Tutor tutor = new Tutor();
    // tutor.setUser(savedUser);
    // tutorRepository.save(tutor); 

//mới thêm vào sau trả về token đăng ký
     // Sinh token
    String token = jwtTokenUtil.generateToken(savedUser.getUsername());

    return ResponseEntity.ok(Map.of(
            "message", "Đăng ký thành công",
            "token", token
    ));  
    // status(HttpStatus.CREATED).body("Đăng ký thành công!");
    // return ResponseEntity.ok("Đăng ký thành công!");
}



   @PostMapping("/student/register")
public ResponseEntity<?> registerStudentUser(@RequestBody User user) {
    // Kiểm tra username đã tồn tại chưa
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        return ResponseEntity
                .badRequest()
                .body("Username đã được sử dụng!");
    }

    // Kiểm tra email đã tồn tại chưa
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        return ResponseEntity
                .badRequest()
                .body("Email đã được sử dụng!");
                
            //     (Mapof()
            //         "message", "Email đã được sử dụng!",
            // "token", null
            //    ););
    }
    user.setRole("STUDENT"); // Mặc định role là tutor

    // Mã hoá mật khẩu trước khi lưu
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    // Lưu user mới vào database
    // userRepository.save(user);
    User savedUser = userRepository.save(user);

    //  Tutor tutor = new Tutor();
    // tutor.setUser(savedUser);
    // tutorRepository.save(tutor); 

//mới thêm vào sau trả về token đăng ký
     // Sinh token
    String token = jwtTokenUtil.generateToken(savedUser.getUsername());

    return ResponseEntity.ok(Map.of(
            "message", "Đăng ký thành công",
            "token", token
    ));  
    // status(HttpStatus.CREATED).body("Đăng ký thành công!");
    // return ResponseEntity.ok("Đăng ký thành công!");
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
            "token", token
    ));
}

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
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
            "token", token
    ));
}

}
