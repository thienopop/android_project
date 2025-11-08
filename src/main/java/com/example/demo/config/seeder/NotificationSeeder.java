package com.example.demo.config.seeder;

import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationSeeder {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(User user, String title, String message,
                                   String type, String redirectUrl, boolean isRead,
                                   LocalDateTime readAt) {

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRedirectUrl(redirectUrl);
        notification.setIsRead(isRead);
        notification.setReadAt(readAt);

        notificationRepository.save(notification);
        return notification;
    }
}
