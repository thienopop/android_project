package com.example.demo.controller;
import com.example.demo.entity.Notification;

import com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.example.demo.entity.Session;


import com.example.demo.entity.Course;

@Service
public class NotificationScheduler {

    @Autowired
    private NotificationRepository notificationRepository;
 @Autowired
    private SessionController sessionController;
    private CourseController courseControler;
    private List<Session> listSession;    
public void getList() {
    // lấy ngày hiện tại của hệ thống
    LocalDate today = LocalDate.now();

    // gọi service và lấy danh sách buổi học trong ngày
    listSession = sessionController.getAllSessions(today);
}


//create notOfsession
    // Chạy mỗi 10 giây
@Scheduled(fixedRate = 10000)
public void generateAutoNotification() {
        
getList();
for (Session session : listSession) {
    LocalDateTime nowDay=LocalDateTime.now();

    Notification notiOfStudent = new Notification();
    Notification notiOfTutor = new Notification();
    if (session.getSessionDate().isAfter(nowDay)) {
        continue;
    }
    Course course=courseControler.getCourseById(session.getCourseId()); 
    notiOfStudent.setUserId(course.getStudent_Id());
    notiOfTutor.setUserId(course.getTutor_Id());
    notiOfStudent.setIsRead(false);
    notiOfTutor.setIsRead(false);
    notiOfStudent.setMessage("Bạn có buổi học môn: " +course.getSubject()+" Hôm nay,vào lúc "+course.getStartTime());
    notiOfTutor.setMessage("Bạn có buổi dạy môn " +course.getSubject()+" Hôm nay, vào lúc "+course.getStartTime());
    notificationRepository.save(notiOfStudent);
    notificationRepository.save(notiOfTutor);
    }

        // noti.setMessage("Thông báo tự động lúc: " + LocalDateTime.now());
        // noti.setCreatedAt(LocalDateTime.now());

        // notificationRepository.save(noti);

        // System.out.println("🔔 Đã tạo thông báo tự động vào DB");
}


//     @Scheduled(cron = "0 0 0 * * *")
// public void generateDailyNotification() {
//     Notification noti = new Notification();

//     noti.setMessage("Thông báo đầu ngày mới: " + LocalDate.now());
//     noti.setCreatedAt(LocalDateTime.now());

//     notificationRepository.save(noti);

//     // System.out.println("🎉 Đã tạo thông báo vào đầu ngày mới");
}





