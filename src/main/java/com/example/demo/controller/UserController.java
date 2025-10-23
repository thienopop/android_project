package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    // // ✅ Tạo người dùng mới (HASH mật khẩu)
    // @PostMapping
    // public User createUser(@RequestBody User user) {
    //     // Nếu người dùng có mật khẩu (từ Android gửi lên)
    //     if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
    //         String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
    //         user.setPasswordHash(hashedPassword);
    //     }
    //     return userRepository.save(user);
    // }


     // ✅ Đăng ký (mã hóa mật khẩu)
    @PostMapping
    public String registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Tên đăng nhập đã tồn tại!";
        }

        // Mã hóa mật khẩu
        String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(hashedPassword);

        userRepository.save(user);
        return "Đăng ký thành công!";
    }

    // ✅ Đăng nhập
    @PostMapping("/login")
    public String login(@RequestBody User loginUser) {
        Optional<User> userOpt = userRepository.findByUsername(loginUser.getUsername());
        if (userOpt.isEmpty()) {
            return "Tài khoản không tồn tại!";
        }

        User user = userOpt.get();
        boolean passwordMatch = passwordEncoder.matches(
                loginUser.getPasswordHash(),
                user.getPasswordHash()
        );

        if (passwordMatch) {
            return user.getId().toString();
        } else {
            return "Sai mật khẩu!";
        }
    }

    // ✅ Cập nhật thông tin người dùng (HASH nếu có thay đổi mật khẩu)
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setFullName(updatedUser.getFullName());
            user.setPhone(updatedUser.getPhone());
            user.setVerified(updatedUser.isVerified());
            user.setName(updatedUser.getName());

            // Nếu người dùng gửi mật khẩu mới -> mã hóa lại
            if (updatedUser.getPasswordHash() != null && !updatedUser.getPasswordHash().isEmpty()) {
                String hashedPassword = passwordEncoder.encode(updatedUser.getPasswordHash());
                user.setPasswordHash(hashedPassword);
            }

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
