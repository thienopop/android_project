package com.example.demo.controller;

import com.example.demo.entity.Course;
import com.example.demo.entity.Notification;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Notification>> findAll() {
        List<Notification> notifications = notificationRepository.findAll();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        if (notification.isEmpty()) {
            return ResponseEntity.status(404).body("Không tìm thấy thông báo với ID = " + id);
        }
        return ResponseEntity.ok(notification.get());
    }
    


    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Notification>> findByUserId(@PathVariable Integer userId) {

        
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return ResponseEntity.ok(notifications);

    }

    
    @GetMapping("/byId/{Id}")
    public ResponseEntity<Notification> findById(@PathVariable int Id) {

        
        Notification notification = notificationRepository.findById(Id);
        return ResponseEntity.ok(notification);

    }

    

    @GetMapping("/by-user")
    public ResponseEntity<List<Notification>> findByUserId(){

   String username = SecurityContextHolder.getContext().getAuthentication().getName();
    
    // 2. Tìm Student (Cẩn thận lỗi Zero Date ở đây nếu chưa fix config DB)
    Optional<User> userOpt = userRepository.findByUsername(username);  // ✅ Đúng
    if (userOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
    }
    // Optional<User> findByUsername(String username);

    // if (userOpt.isEmpty()) {
    //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
    //         "error", "Không tìm thấy thông tin học viên cho tài khoản: " + username
    //     ));
    // }
        List<Notification> notifications = notificationRepository.findByUserId(userOpt.get().getId());
        return ResponseEntity.ok(notifications);

    }


    @PostMapping("/create")
    public ResponseEntity<?> createNotification(@RequestBody Notification notification) {
        if (userRepository.findById(notification.getUserId()).isEmpty()) {
            return ResponseEntity.status(404).body("Không tìm thấy người dùng với ID = " + notification.getUserId());
        }
        Notification savedNotification = notificationRepository.save(notification);
        return ResponseEntity.ok(savedNotification);

    } 
    @PutMapping("update/isread/{id}")
    public ResponseEntity<?> updateReadNotification(@PathVariable Integer id) {
        Optional<Notification> existingNotificationOpt = notificationRepository.findById(id);
        if (existingNotificationOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Không tìm thấy thông báo với ID = " + id);
        }
        Notification existingNotification = existingNotificationOpt.get();

        existingNotification.setIsRead(true);
        // LocalDateTime readAt = notification.getReadAt() != null ? notification.getReadAt() : (notification.getIsRead() ? LocalDateTime.now() : null);
        // existingNotification.setReadAt(readAt);
        Notification updatedNotification = notificationRepository.save(existingNotification);
        return ResponseEntity.ok(updatedNotification);
    
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotification(@PathVariable Integer id, @RequestBody Notification notification) {
        Optional<Notification> existingNotificationOpt = notificationRepository.findById(id);
        if (existingNotificationOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Không tìm thấy thông báo với ID = " + id);
        }
        Notification existingNotification = existingNotificationOpt.get();
        existingNotification.setIsRead(notification.getIsRead());
        LocalDateTime readAt = notification.getReadAt() != null ? notification.getReadAt() : (notification.getIsRead() ? LocalDateTime.now() : null);
        existingNotification.setReadAt(readAt);
        Notification updatedNotification = notificationRepository.save(existingNotification);
        return ResponseEntity.ok(updatedNotification);
    
    }





}
