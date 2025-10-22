package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*") // Cho phép Android app hoặc web gọi API
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // ✅ Lấy toàn bộ người dùng
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Lấy thông tin theo ID
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Integer id) {
        return userRepository.findById(id);
    }

    // ✅ Tạo người dùng mới
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // ✅ Cập nhật thông tin người dùng
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setPasswordHash(updatedUser.getPasswordHash());
            user.setFullName(updatedUser.getFullName());
            user.setPhone(updatedUser.getPhone());
            user.setVerified(updatedUser.isVerified());
            user.setName(updatedUser.getName());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ Xóa người dùng
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
        return "User deleted successfully!";
    }
}
