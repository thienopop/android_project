package com.example.demo.config.seeder;

import com.example.demo.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class Seeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Seeder.class);

    @Autowired
    private UserSeeder userSeeder;
    @Autowired
    private TutorSeeder tutorSeeder;
    @Autowired
    private StudentSeeder studentSeeder;
    @Autowired
    private CourseSeeder courseSeeder;
    @Autowired
    private SessionSeeder sessionSeeder;
    @Autowired
    private ChatSeeder chatSeeder;
    @Autowired
    private MessageSeeder messageSeeder;

    @Value("${app.seed-data:false}")
    private boolean seedData;

    @Override
    public void run(String... args) {
        log.info("Cấu hình khởi tạo dữ liệu mẫu {}:{}", "app.seed-data", seedData);

        if (!seedData) {
            log.info("Bỏ qua bước khởi tạo dữ liệu mẫu");
            return;
        }

        log.info("Bắt đầu khởi tạo dữ liệu mẫu");

        userSeeder.createUser("quantri1", "quantri1@vidu.com", "123", "ADMIN");
        userSeeder.createUser("quantri2", "quantri2@vidu.com", "123", "ADMIN");
        userSeeder.createUser("quantri3", "quantri3@vidu.com", "123", "ADMIN");

        User user1 = userSeeder.createUser("giasu1", "giasu1@vidu.com", "123", "TUTOR");
        User user2 = userSeeder.createUser("giasu2", "giasu2@vidu.com", "123", "TUTOR");
        User user3 = userSeeder.createUser("giasu3", "giasu3@vidu.com", "123", "TUTOR");

        User user4 = userSeeder.createUser("hocvien1", "hocvien1@vidu.com", "123", "STUDENT");
        User user5 = userSeeder.createUser("hocvien2", "hocvien2@vidu.com", "123", "STUDENT");
        User user6 = userSeeder.createUser("hocvien3", "hocvien3@vidu.com", "123", "STUDENT");

        Tutor tutor1 = tutorSeeder.createTutor(user1, "Nguyễn Văn An", "0901234567", "123 Đường Lê Lợi, TP.HCM",
                LocalDate.of(1985, 5, 15), "Gia sư Toán với hơn 10 năm kinh nghiệm dạy kèm học sinh cấp 3",
                10, 300000.0, true, 150, 4.8, "profile_image_1000000000000.jpg");
        Tutor tutor2 = tutorSeeder.createTutor(user2, "Trần Thị Bình", "0907654321", "456 Nguyễn Trãi, Hà Nội",
                LocalDate.of(1990, 8, 22), "Chuyên dạy Lý - Hóa, giúp học sinh hiểu sâu bản chất",
                7, 250000.0, true, 120, 4.7, "profile_image_1000000000000.jpg");
        Tutor tutor3 = tutorSeeder.createTutor(user3, "Lê Hoàng Minh", "0903456789", "789 Võ Văn Kiệt, Đà Nẵng",
                LocalDate.of(1988, 3, 10), "Gia sư tiếng Anh, luyện thi đại học và IELTS",
                8, 350000.0, true, 80, 4.5, "profile_image_1000000000000.jpg");

        Student student1 = studentSeeder.createStudent(user4, "Phạm Thu Hà", "0911234567", "321 Lý Thường Kiệt, TP.HCM",
                LocalDate.of(2008, 6, 20), "Lớp 10", "Yêu thích các môn Khoa học Tự nhiên");
        Student student2 = studentSeeder.createStudent(user5, "Vũ Minh Quân", "0919876543", "654 Hai Bà Trưng, Hà Nội",
                LocalDate.of(2009, 9, 14), "Lớp 9", "Muốn cải thiện điểm Toán và Tiếng Anh");
        Student student3 = studentSeeder.createStudent(user6, "Đỗ Ngọc Lan", "0912345678", "987 Nguyễn Huệ, Huế",
                LocalDate.of(2007, 12, 5), "Lớp 11", "Đang chuẩn bị cho kỳ thi đại học");

        Course course1 = courseSeeder.createCourse(tutor1, student1, "Toán nâng cao", 10, 3_000_000.0, "Thứ 2,4,6 18h-19h30",
                LocalDateTime.of(2025, 11, 10, 18, 0),
                LocalDateTime.of(2025, 11, 10, 19, 30),
                LocalDate.of(2025, 11, 10),
                LocalDate.of(2025, 12, 10),
                "ONGOING", "Lớp học dành cho học sinh giỏi Toán");
        Course course2 = courseSeeder.createCourse(tutor2, student2, "Lý - Hóa cơ bản", 8, 2_000_000.0, "Thứ 3,5 17h-18h30",
                LocalDateTime.of(2025, 11, 12, 17, 0),
                LocalDateTime.of(2025, 11, 12, 18, 30),
                LocalDate.of(2025, 11, 12),
                LocalDate.of(2025, 12, 12),
                "ONGOING", "Củng cố kiến thức Lý và Hóa cho học sinh lớp 9");
        Course course3 = courseSeeder.createCourse(tutor3, student3, "Tiếng Anh luyện thi", 12, 4_500_000.0, "Thứ 2,4 16h-17h30",
                LocalDateTime.of(2025, 11, 11, 16, 0),
                LocalDateTime.of(2025, 11, 11, 17, 30),
                LocalDate.of(2025, 11, 11),
                LocalDate.of(2025, 12, 30),
                "ONGOING", "Luyện thi đại học và IELTS");
        Course course4 = courseSeeder.createCourse(
                tutor1, student2, "Toán cơ bản", 8, 2_500_000.0, "Thứ 2,5 17h-18h30",
                LocalDateTime.of(2025, 11, 15, 17, 0),
                LocalDateTime.of(2025, 11, 15, 18, 30),
                LocalDate.of(2025, 11, 15),
                LocalDate.of(2025, 12, 15),
                "ONGOING", "Lớp học dành cho học sinh cần củng cố Toán cơ bản");
        Course course5 = courseSeeder.createCourse(
                tutor2, student3, "Hóa nâng cao", 10, 3_200_000.0, "Thứ 3,6 18h-19h30",
                LocalDateTime.of(2025, 11, 16, 18, 0),
                LocalDateTime.of(2025, 11, 16, 19, 30),
                LocalDate.of(2025, 11, 16),
                LocalDate.of(2025, 12, 16),
                "ONGOING", "Luyện tập Hóa nâng cao cho học sinh lớp 9-10");
        Course course6 = courseSeeder.createCourse(
                tutor3, student1, "Tiếng Anh giao tiếp", 6, 1_800_000.0, "Thứ 4,7 16h-17h",
                LocalDateTime.of(2025, 11, 17, 16, 0),
                LocalDateTime.of(2025, 11, 17, 17, 0),
                LocalDate.of(2025, 11, 17),
                LocalDate.of(2025, 12, 17),
                "ONGOING", "Cải thiện kỹ năng giao tiếp tiếng Anh cho học sinh lớp 10");
        courseSeeder.createCourse(
                tutor1, null, "Toán chuyên đề hình học", 0, 2_000_000.0,
                "Thứ 2,4 19h-20h30",
                LocalDateTime.of(2025, 11, 20, 19, 0),
                LocalDateTime.of(2025, 11, 20, 20, 30),
                LocalDate.of(2025, 11, 20),
                LocalDate.of(2025, 12, 20),
                "NEW",
                "Khoá học mở, đang chờ học viên đăng ký"
        );
        courseSeeder.createCourse(
                tutor2, null, "Lý – Hóa tổng hợp", 0, 1_800_000.0,
                "Thứ 3,5 19h-20h30",
                LocalDateTime.of(2025, 11, 21, 19, 0),
                LocalDateTime.of(2025, 11, 21, 20, 30),
                LocalDate.of(2025, 11, 21),
                LocalDate.of(2025, 12, 21),
                "NEW",
                "Khoá học mở, đang chờ học viên đăng ký"
        );
        courseSeeder.createCourse(
                tutor3, null, "Tiếng Anh giao tiếp cơ bản", 0, 1_500_000.0,
                "Thứ 6,7 17h-18h30",
                LocalDateTime.of(2025, 11, 22, 17, 0),
                LocalDateTime.of(2025, 11, 22, 18, 30),
                LocalDate.of(2025, 11, 22),
                LocalDate.of(2025, 12, 22),
                "NEW",
                "Khoá học mở, đang chờ học viên đăng ký"
        );
        courseSeeder.createCourse(
                tutor1, student3, "Toán ôn thi học kỳ", 6, 1_800_000.0,
                "Thứ 3,5 18h-19h30",
                LocalDateTime.of(2025, 11, 25, 18, 0),
                LocalDateTime.of(2025, 11, 25, 19, 30),
                LocalDate.of(2025, 11, 25),
                LocalDate.of(2025, 12, 25),
                "STUDENT_REGISTERED",
                "Học viên đã đăng ký, đang chờ gia sư xác nhận"
        );
        courseSeeder.createCourse(
                tutor2, student1, "Lý cơ bản cho học sinh lớp 10", 8, 2_200_000.0,
                "Thứ 2,4 17h-18h30",
                LocalDateTime.of(2025, 11, 26, 17, 0),
                LocalDateTime.of(2025, 11, 26, 18, 30),
                LocalDate.of(2025, 11, 26),
                LocalDate.of(2025, 12, 26),
                "STUDENT_REGISTERED",
                "Khoá học đã có học viên đăng ký, chờ gia sư duyệt"
        );
        courseSeeder.createCourse(
                tutor3, student2, "Tiếng Anh củng cố ngữ pháp", 10, 2_500_000.0,
                "Thứ 6,7 18h-19h30",
                LocalDateTime.of(2025, 11, 27, 18, 0),
                LocalDateTime.of(2025, 11, 27, 19, 30),
                LocalDate.of(2025, 11, 27),
                LocalDate.of(2025, 12, 27),
                "STUDENT_REGISTERED",
                "Học viên đã gửi yêu cầu tham gia khoá học"
        );

        sessionSeeder.createSession(course1, LocalDateTime.of(2025, 11, 10, 18, 0), 90, "SCHEDULED", "Buổi 1: Ôn tập kiến thức cơ bản");
        sessionSeeder.createSession(course1, LocalDateTime.of(2025, 11, 12, 18, 0), 90, "SCHEDULED", "Buổi 2: Giải bài tập nâng cao");
        sessionSeeder.createSession(course2, LocalDateTime.of(2025, 11, 12, 17, 0), 90, "SCHEDULED", "Buổi 1: Giới thiệu chương trình Lý - Hóa");
        sessionSeeder.createSession(course3, LocalDateTime.of(2025, 11, 11, 16, 0), 90, "SCHEDULED", "Buổi 1: Nghe nói cơ bản và luyện đề");
        sessionSeeder.createSession(course4, LocalDateTime.of(2025, 11, 15, 17, 0), 90, "SCHEDULED", "Buổi 1: Giới thiệu chương trình Toán cơ bản");
        sessionSeeder.createSession(course4, LocalDateTime.of(2025, 11, 17, 17, 0), 90, "SCHEDULED", "Buổi 2: Ôn tập chương 1-2");
        sessionSeeder.createSession(course5, LocalDateTime.of(2025, 11, 16, 18, 0), 90, "SCHEDULED", "Buổi 1: Tổng hợp lý thuyết Hóa");
        sessionSeeder.createSession(course5, LocalDateTime.of(2025, 11, 18, 18, 0), 90, "SCHEDULED", "Buổi 2: Giải bài tập nâng cao");
        sessionSeeder.createSession(course6, LocalDateTime.of(2025, 11, 17, 16, 0), 60, "SCHEDULED", "Buổi 1: Làm quen hội thoại cơ bản");
        sessionSeeder.createSession(course6, LocalDateTime.of(2025, 11, 19, 16, 0), 60, "SCHEDULED", "Buổi 2: Thực hành hội thoại theo chủ đề");

        Chat chat1 = chatSeeder.createChat(user4, user1);
        Chat chat2 = chatSeeder.createChat(user5, user2);
        Chat chat3 = chatSeeder.createChat(user6, user3);
        Chat chat4 = chatSeeder.createChat(user5, user1);
        Chat chat5 = chatSeeder.createChat(user4, user3);
        Chat chat6 = chatSeeder.createChat(user6, user2);

        messageSeeder.createMessage(chat1, user1, "Chào Thu Hà, hôm nay chúng ta sẽ ôn tập Toán nâng cao.", null, false, null);
        messageSeeder.createMessage(chat1, user4, "Dạ, thầy An ơi, em đã chuẩn bị bài tập rồi.", null, false, null);
        messageSeeder.createMessage(chat2, user2, "Quân ơi, hôm nay chúng ta học Lý - Hóa nhé.", null, false, null);
        messageSeeder.createMessage(chat2, user5, "Vâng ạ, em đã đọc trước lý thuyết rồi.", null, false, null);
        messageSeeder.createMessage(chat3, user3, "Lan, hôm nay luyện kỹ năng nghe nói Tiếng Anh.", null, false, null);
        messageSeeder.createMessage(chat3, user6, "Vâng ạ.", null, false, null);
        messageSeeder.createMessage(chat4, user1, "Chào Quân, hôm nay chúng ta học Toán cơ bản nhé.", null, false, null);
        messageSeeder.createMessage(chat4, user5, "Dạ, em đã chuẩn bị bài tập.", null, false, null);
        messageSeeder.createMessage(chat5, user3, "Chào Thu Hà, hôm nay luyện Tiếng Anh giao tiếp.", null, false, null);
        messageSeeder.createMessage(chat5, user4, "Em sẵn sàng ạ.", null, false, null);
        messageSeeder.createMessage(chat6, user2, "Lan, hôm nay học Hóa nâng cao nhé.", null, false, null);
        messageSeeder.createMessage(chat6, user6, "Vâng, em đã chuẩn bị lý thuyết.", null, false, null);

        log.info("Hoàn tất khởi tạo dữ liệu mẫu");
    }
}
