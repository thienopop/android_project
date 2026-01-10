package com.example.demo.controller;

import com.example.demo.entity.Notification;
import com.example.demo.entity.Session;
import com.example.demo.entity.Course;
import com.example.demo.entity.Tutor;
import com.example.demo.entity.User;
import com.example.demo.entity.Student;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.TutorRepository;
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
    private CourseController courseController;
        @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TutorRepository tutorRepository;

    private List<Session> listSession;
    public void getList() {
        String dateStr = LocalDate.now().toString() + " 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDate date = LocalDateTime.parse(dateStr, formatter).toLocalDate();
        listSession = sessionController.getAllSessions(date);
    }
    
//  @Scheduled(cron = "0 0 0 * * *")
 @Scheduled(fixedRate = 10000)
public void generateAutoNotification() {
//lấy danh sách khoá học hôm nay
    getList();


    if (listSession == null || listSession.isEmpty()) {
        return;
    }

    for (Session session : listSession) {

        //kiểm tra nếu thời gian buổi học đã qua thì bỏ qua
        if(session.getSessionDate()==null || !session.getSessionDate().isAfter(LocalDateTime.now())) {

            continue;
        }

        Course course = courseController.getCourseById(session.getCourseId());
        if (course == null) {
            System.out.println("Course NULL for sessionId = " + session.getId());
            continue;
        }

        Integer studentId = course.getStudent_Id();
        Integer tutorId = course.getTutor_Id();

        if (studentId == null || studentId <= 0) {
            System.out.println("student_id NULL for courseId = " + course.getId());
            continue;
        }
        if (tutorId == null || tutorId <= 0) {
            System.out.println("tutor_id NULL for courseId = " + course.getId());
            continue;
        }

        //  Lấy entity User cho student
// Lấy danh thông tin stuent
Student stu = studentRepository.findById(studentId).orElse(null);
// lấy user của student 

        User student = stu.getUser();

          System.out.println(" student____ID " + studentId);
        if (student == null) {
            System.out.println("Student user NOT FOUND: " + studentId);
            continue;
        }
        Tutor tutorEntity = tutorRepository.findById(tutorId).orElse(null);
// lấy user của student 
        User tutor = tutorEntity.getUser();
         System.out.println(" tutor____ID " + tutorId);
        if (tutor == null) {
            System.out.println("Tutor user NOT FOUND: " + tutorId);
            continue;
        }
        //  Gửi thông báo cho student
        Notification notiStudent = new Notification();
        notiStudent.setUser(student);  
        notiStudent.setIsRead(false);
        notiStudent.setCreatedAt(LocalDateTime.now());
        notiStudent.setTitle("Nhắc lịch học");
        notiStudent.setMessage("Bạn có buổi học môn " + course.getSubject()
                + " hôm nay, vào lúc " + course.getStartTime());

        // Gửi thông báo cho tutor
        Notification notiTutor = new Notification();
        notiTutor.setUser(tutor);  
        notiTutor.setIsRead(false);
        notiTutor.setCreatedAt(LocalDateTime.now());
        notiTutor.setTitle("Nhắc lịch dạy");
        notiTutor.setMessage("Bạn có buổi dạy môn " + course.getSubject()
                + " hôm nay, vào lúc " + course.getStartTime());

        notificationRepository.save(notiStudent);
        notificationRepository.save(notiTutor);
    }
}

}
