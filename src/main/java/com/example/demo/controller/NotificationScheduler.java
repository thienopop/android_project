package com.example.demo.controller;

import com.example.demo.entity.Notification;
import com.example.demo.entity.Session;
import com.example.demo.entity.Course;
import com.example.demo.entity.Tutor;
import com.example.demo.entity.User;
import com.example.demo.entity.Student;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.time.format.DateTimeFormatter;


@Service
public class NotificationScheduler {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SessionController sessionController;

    @Autowired
     private CourseRepository courseRepository;
    @Autowired
    private CourseController courseController;
    @Autowired
    private UserRepository userRepository;

    private List<Session> listSession;


    public void getList() {
        String dateStr = LocalDate.now().toString() + " 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDate date = LocalDateTime.parse(dateStr, formatter).toLocalDate();
        listSession = sessionController.getAllSessions(date);
    }


//     @Scheduled(fixedRate = 10000)
//     public void generateAutoNotification() {

//         getList();
       

//         if (listSession == null || listSession.isEmpty()) {
//             return;
//         }

//         for (Session session : listSession) {
//  Course course = null;
//         course = courseControler.getCourseById(session.getCourseId());

//             // CHẶN LỖI NULL
//             if (course == null) {
//                 System.out.println("⚠ Course NULL for sessionId = " + session.getId());
//                 continue;
//             }

//             if (course.getStudent_Id() <=0) {
//                 System.out.println("⚠ student_id NULL for courseId = " + course.getId());
//                 continue;
//             }


// Integer tutorId=course.getTutor_Id();
//             if (tutorId<=0) {
//                 System.out.println("⚠ tutor_id NULL for courseId = " + course.getId());
//                 continue;
//             }

//             Integer studentId = course.getStudent_Id();
// if (studentId<=0) {
//     continue; // bỏ qua nếu không có student
// }

//             // 🔔 Gửi thông báo cho student
//             Notification notiStudent = new Notification();
//             notiStudent.setUserId(studentId);
//             notiStudent.setIsRead(false);
//             notiStudent.setCreatedAt(LocalDateTime.now());
//             notiStudent.setTitle("Nhắc lịch học");
//             notiStudent.setMessage("Bạn có buổi học môn " + course.getSubject()
//                     + " hôm nay, vào lúc " + course.getStartTime());

//             // 🔔 Gửi thông báo cho tutor
//             Notification notiTutor = new Notification();
//             notiTutor.setUserId(tutorId);
//             notiTutor.setIsRead(false);
//             notiTutor.setCreatedAt(LocalDateTime.now());
//             notiTutor.setTitle("Nhắc lịch dạy");
//             notiTutor.setMessage("Bạn có buổi dạy môn " + course.getSubject()
//                     + " hôm nay, vào lúc " + course.getStartTime());

//             notificationRepository.save(notiStudent);
//             notificationRepository.save(notiTutor);
//         }
//     }

//  @Scheduled(cron = "0 0 0 * * *")
 @Scheduled(fixedRate = 10000)
public void generateAutoNotification() {

    getList();

    if (listSession == null || listSession.isEmpty()) {
        return;
    }

    for (Session session : listSession) {

        Course course = courseController.getCourseById(session.getCourseId());
        if (course == null) {
            System.out.println("⚠ Course NULL for sessionId = " + session.getId());
            continue;
        }

        Integer studentId = course.getStudent_Id();
        Integer tutorId = course.getTutor_Id();

        if (studentId == null || studentId <= 0) {
            System.out.println("⚠ student_id NULL for courseId = " + course.getId());
            continue;
        }
        if (tutorId == null || tutorId <= 0) {
            System.out.println("⚠ tutor_id NULL for courseId = " + course.getId());
            continue;
        }

        // 🧑‍🎓 Lấy entity User cho student
        User student = userRepository.findById(studentId).orElse(null);
        if (student == null) {
            System.out.println("⚠ Student user NOT FOUND: " + studentId);
            continue;
        }

        // 🧑‍🏫 Lấy entity User cho tutor
        User tutor = userRepository.findById(tutorId).orElse(null);
        if (tutor == null) {
            System.out.println("⚠ Tutor user NOT FOUND: " + tutorId);
            continue;
        }

        // 🔔 Gửi thông báo cho student
        Notification notiStudent = new Notification();
        notiStudent.setUser(student);  // QUAN TRỌNG
        notiStudent.setIsRead(false);
        notiStudent.setCreatedAt(LocalDateTime.now());
        notiStudent.setTitle("Nhắc lịch học");
        notiStudent.setMessage("Bạn có buổi học môn " + course.getSubject()
                + " hôm nay, vào lúc " + course.getStartTime());

        // 🔔 Gửi thông báo cho tutor
        Notification notiTutor = new Notification();
        notiTutor.setUser(tutor);  // QUAN TRỌNG
        notiTutor.setIsRead(false);
        notiTutor.setCreatedAt(LocalDateTime.now());
        notiTutor.setTitle("Nhắc lịch dạy");
        notiTutor.setMessage("Bạn có buổi dạy môn " + course.getSubject()
                + " hôm nay, vào lúc " + course.getStartTime());

        notificationRepository.save(notiStudent);
        notificationRepository.save(notiTutor);
    }
}

    // @Scheduled(cron = "0 0 0 * * *")
    // public void generateDailyNotification() {
    //     Notification noti = new Notification();
    //     noti.setMessage("Thông báo đầu ngày mới: " + LocalDate.now());
    //     noti.setIsRead(false);
    //     noti.setCreatedAt(LocalDateTime.now());

    //     notificationRepository.save(noti);
    // }
}
