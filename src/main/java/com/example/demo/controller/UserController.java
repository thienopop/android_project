package com.example.demo.controller;

import com.example.demo.config.JwtTokenUtil;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
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
                .body("Tên đăng nhập đã tồn tại!");
    }

    // Kiểm tra email đã tồn tại chưa
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        return ResponseEntity
                .badRequest()
                .body("Email đã được sử dụng!");
    }

    // Mã hoá mật khẩu trước khi lưu
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    // Lưu user mới vào database
    userRepository.save(user);

    return ResponseEntity.ok("Đăng ký thành công!");
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
                .body(Map.of("error", "Người dùng không tồn tại"));
    }

    // Kiểm tra mật khẩu
    if (!passwordEncoder.matches(password, user.getPassword())) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Sai mật khẩu!"));
    }

    // Sinh token
    String token = jwtTokenUtil.generateToken(user.getUsername());

    return ResponseEntity.ok(Map.of(
            "message", "Đăng nhập thành công",
            "token", token
    ));
}

}
