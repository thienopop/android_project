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
    @Autowired
    private FeedbackSeeder feedbackSeeder;

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

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate tomorrow = today.plusDays(1);
        LocalDate oneWeekAgo = today.minusWeeks(1);
        LocalDate oneWeekLater = today.plusWeeks(1);
        LocalDate twoWeeksLater = today.plusWeeks(2);
        LocalDate oneMonthAgo = today.minusMonths(1);
        LocalDate oneMonthLater = today.plusMonths(1);
        LocalDate twoMonthsAgo = today.minusMonths(2);
        LocalDate threeMonthsAgo = today.minusMonths(3);

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
                10, 300000.0, true, 150, 5.0, "profile_image_1000000000000.jpg");
        Tutor tutor2 = tutorSeeder.createTutor(user2, "Trần Thị Bình", "0907654321", "456 Nguyễn Trãi, Hà Nội",
                LocalDate.of(1990, 8, 22), "Chuyên dạy Lý - Hóa, giúp học sinh hiểu sâu bản chất",
                7, 250000.0, true, 120, 5.0, "profile_image_1000000000000.jpg");
        Tutor tutor3 = tutorSeeder.createTutor(user3, "Lê Hoàng Minh", "0903456789", "789 Võ Văn Kiệt, Đà Nẵng",
                LocalDate.of(1988, 3, 10), "Gia sư tiếng Anh, luyện thi đại học và IELTS",
                8, 350000.0, true, 80, 4.0, "profile_image_1000000000000.jpg");

        Student student1 = studentSeeder.createStudent(user4, "Phạm Thu Hà", "0911234567", "321 Lý Thường Kiệt, TP.HCM",
                LocalDate.of(2008, 6, 20), "Lớp 10", "Yêu thích các môn Khoa học Tự nhiên");
        Student student2 = studentSeeder.createStudent(user5, "Vũ Minh Quân", "0919876543", "654 Hai Bà Trưng, Hà Nội",
                LocalDate.of(2009, 9, 14), "Lớp 9", "Muốn cải thiện điểm Toán và Tiếng Anh");
        Student student3 = studentSeeder.createStudent(user6, "Đỗ Ngọc Lan", "0912345678", "987 Nguyễn Huệ, Huế",
                LocalDate.of(2007, 12, 5), "Lớp 11", "Đang chuẩn bị cho kỳ thi đại học");

        Course course1 = courseSeeder.createCourse(tutor1, student1, "Toán nâng cao", 10, 3_000_000.0, "Thứ 2,4,6 18h-19h30",
                LocalDateTime.of(oneWeekAgo.getYear(), oneWeekAgo.getMonthValue(), oneWeekAgo.getDayOfMonth(), 18, 0),
                LocalDateTime.of(oneWeekAgo.getYear(), oneWeekAgo.getMonthValue(), oneWeekAgo.getDayOfMonth(), 19, 30),
                oneWeekAgo,
                oneMonthLater,
                "ONGOING", "Lớp học dành cho học sinh giỏi Toán");
        Course course2 = courseSeeder.createCourse(tutor2, student2, "Lý - Hóa cơ bản", 8, 2_000_000.0, "Thứ 3,5 17h-18h30",
                LocalDateTime.of(oneWeekAgo.getYear(), oneWeekAgo.getMonthValue(), oneWeekAgo.getDayOfMonth(), 17, 0),
                LocalDateTime.of(oneWeekAgo.getYear(), oneWeekAgo.getMonthValue(), oneWeekAgo.getDayOfMonth(), 18, 30),
                oneWeekAgo,
                oneMonthLater,
                "ONGOING", "Củng cố kiến thức Lý và Hóa cho học sinh lớp 9");
        Course course3 = courseSeeder.createCourse(tutor3, student3, "Tiếng Anh luyện thi", 12, 4_500_000.0, "Thứ 2,4 16h-17h30",
                LocalDateTime.of(oneWeekAgo.getYear(), oneWeekAgo.getMonthValue(), oneWeekAgo.getDayOfMonth(), 16, 0),
                LocalDateTime.of(oneWeekAgo.getYear(), oneWeekAgo.getMonthValue(), oneWeekAgo.getDayOfMonth(), 17, 30),
                oneWeekAgo,
                oneMonthLater.plusWeeks(3),
                "ONGOING", "Luyện thi đại học và IELTS");
        Course course4 = courseSeeder.createCourse(
                tutor1, student2, "Toán cơ bản", 8, 2_500_000.0, "Thứ 2,5 17h-18h30",
                LocalDateTime.of(yesterday.getYear(), yesterday.getMonthValue(), yesterday.getDayOfMonth(), 17, 0),
                LocalDateTime.of(yesterday.getYear(), yesterday.getMonthValue(), yesterday.getDayOfMonth(), 18, 30),
                yesterday,
                oneMonthLater,
                "ONGOING", "Lớp học dành cho học sinh cần củng cố Toán cơ bản");
        Course course5 = courseSeeder.createCourse(
                tutor2, student3, "Hóa nâng cao", 10, 3_200_000.0, "Thứ 3,6 18h-19h30",
                LocalDateTime.of(yesterday.getYear(), yesterday.getMonthValue(), yesterday.getDayOfMonth(), 18, 0),
                LocalDateTime.of(yesterday.getYear(), yesterday.getMonthValue(), yesterday.getDayOfMonth(), 19, 30),
                yesterday,
                oneMonthLater,
                "ONGOING", "Luyện tập Hóa nâng cao cho học sinh lớp 9-10");
        Course course6 = courseSeeder.createCourse(
                tutor3, student1, "Tiếng Anh giao tiếp", 6, 1_800_000.0, "Thứ 4,7 16h-17h",
                LocalDateTime.of(yesterday.getYear(), yesterday.getMonthValue(), yesterday.getDayOfMonth(), 16, 0),
                LocalDateTime.of(yesterday.getYear(), yesterday.getMonthValue(), yesterday.getDayOfMonth(), 17, 0),
                yesterday,
                oneMonthLater,
                "ONGOING", "Cải thiện kỹ năng giao tiếp tiếng Anh cho học sinh lớp 10");
        Course completedCourse1 = courseSeeder.createCourse(
                tutor1, student1, "Toán tổng ôn đại học", 4, 1_600_000.0, "Thứ 2,4 19h-20h30",
                LocalDateTime.of(twoMonthsAgo.getYear(), twoMonthsAgo.getMonthValue(), twoMonthsAgo.getDayOfMonth(), 19, 0),
                LocalDateTime.of(twoMonthsAgo.getYear(), twoMonthsAgo.getMonthValue(), twoMonthsAgo.getDayOfMonth(), 20, 30),
                twoMonthsAgo,
                oneMonthAgo.minusWeeks(1),
                "COMPLETED", "Khóa học đã hoàn thành, ôn tập toàn bộ chuyên đề trọng tâm"
        );
        Course completedCourse2 = courseSeeder.createCourse(
                tutor2, student2, "Lý – Hóa ôn thi cấp tốc", 4, 1_400_000.0, "Thứ 3,5 18h-19h30",
                LocalDateTime.of(twoMonthsAgo.getYear(), twoMonthsAgo.getMonthValue(), twoMonthsAgo.getDayOfMonth(), 18, 0),
                LocalDateTime.of(twoMonthsAgo.getYear(), twoMonthsAgo.getMonthValue(), twoMonthsAgo.getDayOfMonth(), 19, 30),
                twoMonthsAgo,
                oneMonthAgo.minusWeeks(1),
                "COMPLETED", "Hệ thống hóa kiến thức và luyện đề thi thử"
        );
        Course completedCourse3 = courseSeeder.createCourse(
                tutor3, student3, "Tiếng Anh luyện đề đại học", 4, 1_800_000.0, "Thứ 6,7 18h-19h30",
                LocalDateTime.of(twoMonthsAgo.getYear(), twoMonthsAgo.getMonthValue(), twoMonthsAgo.getDayOfMonth(), 18, 0),
                LocalDateTime.of(twoMonthsAgo.getYear(), twoMonthsAgo.getMonthValue(), twoMonthsAgo.getDayOfMonth(), 19, 30),
                twoMonthsAgo,
                oneMonthAgo.minusWeeks(1),
                "COMPLETED", "Luyện đề, sửa lỗi chi tiết, nâng band điểm"
        );
        Course completedCourse4 = courseSeeder.createCourse(
                tutor1, student3, "Toán luyện đề thi học kỳ 1", 3, 1_350_000.0,
                "Thứ 2,5 19h-20h30",
                LocalDateTime.of(threeMonthsAgo.getYear(), threeMonthsAgo.getMonthValue(), threeMonthsAgo.getDayOfMonth(), 19, 0),
                LocalDateTime.of(threeMonthsAgo.getYear(), threeMonthsAgo.getMonthValue(), threeMonthsAgo.getDayOfMonth(), 20, 30),
                threeMonthsAgo,
                twoMonthsAgo.minusWeeks(1),
                "COMPLETED", "Ôn tập và luyện đề thi học kỳ 1, nâng cao khả năng làm bài");
        Course completedCourse5 = courseSeeder.createCourse(
                tutor2, student1, "Lý thuyết Hóa trọng tâm", 3, 1_200_000.0,
                "Thứ 3,6 18h-19h30",
                LocalDateTime.of(threeMonthsAgo.getYear(), threeMonthsAgo.getMonthValue(), threeMonthsAgo.getDayOfMonth(), 18, 0),
                LocalDateTime.of(threeMonthsAgo.getYear(), threeMonthsAgo.getMonthValue(), threeMonthsAgo.getDayOfMonth(), 19, 30),
                threeMonthsAgo,
                twoMonthsAgo.minusWeeks(1),
                "COMPLETED", "Hệ thống hóa lý thuyết Hóa, củng cố kiến thức cơ bản");
        Course completedCourse6 = courseSeeder.createCourse(
                tutor3, student2, "Tiếng Anh ngữ pháp cơ bản", 3, 1_050_000.0,
                "Thứ 4,7 17h-18h30",
                LocalDateTime.of(threeMonthsAgo.getYear(), threeMonthsAgo.getMonthValue(), threeMonthsAgo.getDayOfMonth(), 17, 0),
                LocalDateTime.of(threeMonthsAgo.getYear(), threeMonthsAgo.getMonthValue(), threeMonthsAgo.getDayOfMonth(), 18, 30),
                threeMonthsAgo,
                twoMonthsAgo.minusWeeks(1),
                "COMPLETED", "Củng cố ngữ pháp tiếng Anh cơ bản, nền tảng giao tiếp");
        courseSeeder.createCourse(
                tutor1, null, "Toán chuyên đề hình học", 0, 2_000_000.0,
                "Thứ 2,4 19h-20h30",
                LocalDateTime.of(oneWeekLater.getYear(), oneWeekLater.getMonthValue(), oneWeekLater.getDayOfMonth(), 19, 0),
                LocalDateTime.of(oneWeekLater.getYear(), oneWeekLater.getMonthValue(), oneWeekLater.getDayOfMonth(), 20, 30),
                oneWeekLater,
                twoWeeksLater.plusWeeks(2),
                "NEW",
                "Lớp toán chuyên đề hình học"
        );
        courseSeeder.createCourse(
                tutor2, null, "Lý – Hóa tổng hợp", 0, 1_800_000.0,
                "Thứ 3,5 19h-20h30",
                LocalDateTime.of(oneWeekLater.getYear(), oneWeekLater.getMonthValue(), oneWeekLater.getDayOfMonth(), 19, 0),
                LocalDateTime.of(oneWeekLater.getYear(), oneWeekLater.getMonthValue(), oneWeekLater.getDayOfMonth(), 20, 30),
                oneWeekLater,
                twoWeeksLater.plusWeeks(2),
                "NEW",
                "Lớp học tổng hợp Lý và Hóa dành cho học sinh cấp 2"
        );
        courseSeeder.createCourse(
                tutor3, null, "Tiếng Anh giao tiếp cơ bản", 0, 1_500_000.0,
                "Thứ 6,7 17h-18h30",
                LocalDateTime.of(twoWeeksLater.getYear(), twoWeeksLater.getMonthValue(), twoWeeksLater.getDayOfMonth(), 17, 0),
                LocalDateTime.of(twoWeeksLater.getYear(), twoWeeksLater.getMonthValue(), twoWeeksLater.getDayOfMonth(), 18, 30),
                twoWeeksLater,
                oneMonthLater.plusWeeks(1),
                "NEW",
                "Lớp học tiếng Anh giao tiếp cơ bản cho người mới bắt đầu"
        );
        courseSeeder.createCourse(
                tutor1, student3, "Toán ôn thi học kỳ", 6, 1_800_000.0,
                "Thứ 3,5 18h-19h30",
                LocalDateTime.of(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth(), 18, 0),
                LocalDateTime.of(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth(), 19, 30),
                tomorrow,
                oneMonthLater,
                "STUDENT_REGISTERED",
                "Lớp toán ôn thi"
        );
        courseSeeder.createCourse(
                tutor2, student1, "Lý cơ bản cho học sinh lớp 10", 8, 2_200_000.0,
                "Thứ 2,4 17h-18h30",
                LocalDateTime.of(tomorrow.plusDays(1).getYear(), tomorrow.plusDays(1).getMonthValue(), tomorrow.plusDays(1).getDayOfMonth(), 17, 0),
                LocalDateTime.of(tomorrow.plusDays(1).getYear(), tomorrow.plusDays(1).getMonthValue(), tomorrow.plusDays(1).getDayOfMonth(), 18, 30),
                tomorrow.plusDays(1),
                oneMonthLater,
                "STUDENT_REGISTERED",
                "Lớp học Lý cơ bản dành cho học sinh lớp 10"
        );
        courseSeeder.createCourse(
                tutor3, student2, "Tiếng Anh củng cố ngữ pháp", 10, 2_500_000.0,
                "Thứ 6,7 18h-19h30",
                LocalDateTime.of(tomorrow.plusDays(2).getYear(), tomorrow.plusDays(2).getMonthValue(), tomorrow.plusDays(2).getDayOfMonth(), 18, 0),
                LocalDateTime.of(tomorrow.plusDays(2).getYear(), tomorrow.plusDays(2).getMonthValue(), tomorrow.plusDays(2).getDayOfMonth(), 19, 30),
                tomorrow.plusDays(2),
                oneMonthLater,
                "STUDENT_REGISTERED",
                "Lớp học củng cố ngữ pháp tiếng Anh"
        );
        courseSeeder.createCourse(
                tutor1, null, "Toán ôn tập giữa kỳ", 3, 1_200_000.0,
                "Thứ 3,6 17h-18h30",
                LocalDateTime.of(oneMonthAgo.getYear(), oneMonthAgo.getMonthValue(), oneMonthAgo.getDayOfMonth(), 17, 0),
                LocalDateTime.of(oneMonthAgo.getYear(), oneMonthAgo.getMonthValue(), oneMonthAgo.getDayOfMonth(), 18, 30),
                oneMonthAgo,
                oneWeekAgo,
                "CANCELLED", "Lớp toán ôn tâập giữa kỳ");

        courseSeeder.createCourse(
                tutor2, null, "Hóa cơ bản lớp 9", 3, 1_000_000.0,
                "Thứ 4,7 18h-19h30",
                LocalDateTime.of(oneMonthAgo.getYear(), oneMonthAgo.getMonthValue(), oneMonthAgo.getDayOfMonth(), 18, 0),
                LocalDateTime.of(oneMonthAgo.getYear(), oneMonthAgo.getMonthValue(), oneMonthAgo.getDayOfMonth(), 19, 30),
                oneMonthAgo,
                oneWeekAgo,
                "CANCELLED", "Lớp hóa học cơ bản lớp 9");

        courseSeeder.createCourse(
                tutor3, null, "Tiếng Anh cơ bản", 3, 1_100_000.0,
                "Thứ 5,7 16h-17h30",
                LocalDateTime.of(oneMonthAgo.getYear(), oneMonthAgo.getMonthValue(), oneMonthAgo.getDayOfMonth(), 16, 0),
                LocalDateTime.of(oneMonthAgo.getYear(), oneMonthAgo.getMonthValue(), oneMonthAgo.getDayOfMonth(), 17, 30),
                oneMonthAgo,
                oneWeekAgo,
                "CANCELLED", "Lớp tiếng Anh cơ bản cho học sinh cấp 2");

        sessionSeeder.createSession(course1, LocalDateTime.of(oneWeekAgo.getYear(), oneWeekAgo.getMonthValue(), oneWeekAgo.getDayOfMonth(), 18, 0), 90, "COMPLETED", "Buổi 1: Ôn tập kiến thức cơ bản");
        sessionSeeder.createSession(course1, LocalDateTime.of(oneWeekAgo.plusDays(2).getYear(), oneWeekAgo.plusDays(2).getMonthValue(), oneWeekAgo.plusDays(2).getDayOfMonth(), 18, 0), 90, "COMPLETED", "Buổi 2: Giải bài tập nâng cao");
        sessionSeeder.createSession(course1, LocalDateTime.of(today.getYear(), today.getMonthValue(), today.getDayOfMonth(), 18, 0), 90, "SCHEDULED", "Buổi 3: Luyện tập chuyên sâu");
        sessionSeeder.createSession(course2, LocalDateTime.of(oneWeekAgo.getYear(), oneWeekAgo.getMonthValue(), oneWeekAgo.getDayOfMonth(), 17, 0), 90, "COMPLETED", "Buổi 1: Giới thiệu chương trình Lý - Hóa");
        sessionSeeder.createSession(course2, LocalDateTime.of(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth(), 17, 0), 90, "SCHEDULED", "Buổi 2: Bài tập Lý");
        sessionSeeder.createSession(course3, LocalDateTime.of(oneWeekAgo.getYear(), oneWeekAgo.getMonthValue(), oneWeekAgo.getDayOfMonth(), 16, 0), 90, "COMPLETED", "Buổi 1: Nghe nói cơ bản và luyện đề");
        sessionSeeder.createSession(course3, LocalDateTime.of(tomorrow.plusDays(1).getYear(), tomorrow.plusDays(1).getMonthValue(), tomorrow.plusDays(1).getDayOfMonth(), 16, 0), 90, "SCHEDULED", "Buổi 2: Grammar and vocabulary");
        sessionSeeder.createSession(course4, LocalDateTime.of(yesterday.getYear(), yesterday.getMonthValue(), yesterday.getDayOfMonth(), 17, 0), 90, "COMPLETED", "Buổi 1: Giới thiệu chương trình Toán cơ bản");
        sessionSeeder.createSession(course4, LocalDateTime.of(tomorrow.plusDays(2).getYear(), tomorrow.plusDays(2).getMonthValue(), tomorrow.plusDays(2).getDayOfMonth(), 17, 0), 90, "SCHEDULED", "Buổi 2: Ôn tập chương 1-2");
        sessionSeeder.createSession(course5, LocalDateTime.of(yesterday.getYear(), yesterday.getMonthValue(), yesterday.getDayOfMonth(), 18, 0), 90, "COMPLETED", "Buổi 1: Tổng hợp lý thuyết Hóa");
        sessionSeeder.createSession(course5, LocalDateTime.of(tomorrow.plusDays(3).getYear(), tomorrow.plusDays(3).getMonthValue(), tomorrow.plusDays(3).getDayOfMonth(), 18, 0), 90, "SCHEDULED", "Buổi 2: Giải bài tập nâng cao");
        sessionSeeder.createSession(course6, LocalDateTime.of(yesterday.getYear(), yesterday.getMonthValue(), yesterday.getDayOfMonth(), 16, 0), 60, "COMPLETED", "Buổi 1: Làm quen hội thoại cơ bản");
        sessionSeeder.createSession(course6, LocalDateTime.of(tomorrow.plusDays(1).getYear(), tomorrow.plusDays(1).getMonthValue(), tomorrow.plusDays(1).getDayOfMonth(), 16, 0), 60, "SCHEDULED", "Buổi 2: Thực hành hội thoại theo chủ đề");
        sessionSeeder.createSession(completedCourse1, LocalDateTime.of(twoMonthsAgo.getYear(), twoMonthsAgo.getMonthValue(), twoMonthsAgo.getDayOfMonth(), 19, 0), 90, "COMPLETED", "Buổi 1: Tổng quan đề thi");
        sessionSeeder.createSession(completedCourse1, LocalDateTime.of(twoMonthsAgo.plusDays(3).getYear(), twoMonthsAgo.plusDays(3).getMonthValue(), twoMonthsAgo.plusDays(3).getDayOfMonth(), 19, 0), 90, "COMPLETED", "Buổi 2: Hàm số và đồ thị");
        sessionSeeder.createSession(completedCourse1, LocalDateTime.of(twoMonthsAgo.plusWeeks(1).getYear(), twoMonthsAgo.plusWeeks(1).getMonthValue(), twoMonthsAgo.plusWeeks(1).getDayOfMonth(), 19, 0), 90, "COMPLETED", "Buổi 3: Hình học không gian");
        sessionSeeder.createSession(completedCourse1, LocalDateTime.of(twoMonthsAgo.plusWeeks(2).getYear(), twoMonthsAgo.plusWeeks(2).getMonthValue(), twoMonthsAgo.plusWeeks(2).getDayOfMonth(), 19, 0), 90, "COMPLETED", "Buổi 4: Luyện đề tổng hợp");
        sessionSeeder.createSession(completedCourse2, LocalDateTime.of(twoMonthsAgo.getYear(), twoMonthsAgo.getMonthValue(), twoMonthsAgo.getDayOfMonth(), 18, 0), 90, "COMPLETED", "Buổi 1: Ôn Lý cơ bản");
        sessionSeeder.createSession(completedCourse2, LocalDateTime.of(twoMonthsAgo.plusDays(3).getYear(), twoMonthsAgo.plusDays(3).getMonthValue(), twoMonthsAgo.plusDays(3).getDayOfMonth(), 18, 0), 90, "COMPLETED", "Buổi 2: Bài tập Lý");
        sessionSeeder.createSession(completedCourse2, LocalDateTime.of(twoMonthsAgo.plusWeeks(1).getYear(), twoMonthsAgo.plusWeeks(1).getMonthValue(), twoMonthsAgo.plusWeeks(1).getDayOfMonth(), 18, 0), 90, "COMPLETED", "Buổi 3: Ôn Hóa trọng tâm");
        sessionSeeder.createSession(completedCourse2, LocalDateTime.of(twoMonthsAgo.plusWeeks(2).getYear(), twoMonthsAgo.plusWeeks(2).getMonthValue(), twoMonthsAgo.plusWeeks(2).getDayOfMonth(), 18, 0), 90, "COMPLETED", "Buổi 4: Luyện đề kết hợp");
        sessionSeeder.createSession(completedCourse3, LocalDateTime.of(twoMonthsAgo.getYear(), twoMonthsAgo.getMonthValue(), twoMonthsAgo.getDayOfMonth(), 18, 0), 90, "COMPLETED", "Buổi 1: Reading strategies");
        sessionSeeder.createSession(completedCourse3, LocalDateTime.of(twoMonthsAgo.plusDays(3).getYear(), twoMonthsAgo.plusDays(3).getMonthValue(), twoMonthsAgo.plusDays(3).getDayOfMonth(), 18, 0), 90, "COMPLETED", "Buổi 2: Grammar pitfalls");
        sessionSeeder.createSession(completedCourse3, LocalDateTime.of(twoMonthsAgo.plusWeeks(1).getYear(), twoMonthsAgo.plusWeeks(1).getMonthValue(), twoMonthsAgo.plusWeeks(1).getDayOfMonth(), 18, 0), 90, "COMPLETED", "Buổi 3: Writing task");
        sessionSeeder.createSession(completedCourse3, LocalDateTime.of(twoMonthsAgo.plusWeeks(2).getYear(), twoMonthsAgo.plusWeeks(2).getMonthValue(), twoMonthsAgo.plusWeeks(2).getDayOfMonth(), 18, 0), 90, "COMPLETED", "Buổi 4: Full test practice");
        sessionSeeder.createSession(completedCourse4, LocalDateTime.of(threeMonthsAgo.getYear(), threeMonthsAgo.getMonthValue(), threeMonthsAgo.getDayOfMonth(), 19, 0), 90, "COMPLETED", "Buổi 1: Ôn tập chuyên đề hàm số");
        sessionSeeder.createSession(completedCourse4, LocalDateTime.of(threeMonthsAgo.plusDays(4).getYear(), threeMonthsAgo.plusDays(4).getMonthValue(), threeMonthsAgo.plusDays(4).getDayOfMonth(), 19, 0), 90, "COMPLETED", "Buổi 2: Bài tập tọa độ và vectơ");
        sessionSeeder.createSession(completedCourse4, LocalDateTime.of(threeMonthsAgo.plusWeeks(1).plusDays(3).getYear(), threeMonthsAgo.plusWeeks(1).plusDays(3).getMonthValue(), threeMonthsAgo.plusWeeks(1).plusDays(3).getDayOfMonth(), 19, 0), 90, "COMPLETED", "Buổi 3: Luyện đề tổng hợp học kỳ");
        sessionSeeder.createSession(completedCourse5, LocalDateTime.of(threeMonthsAgo.getYear(), threeMonthsAgo.getMonthValue(), threeMonthsAgo.getDayOfMonth(), 18, 0), 90, "COMPLETED", "Buổi 1: Lý thuyết phản ứng hóa học");
        sessionSeeder.createSession(completedCourse5, LocalDateTime.of(threeMonthsAgo.plusDays(4).getYear(), threeMonthsAgo.plusDays(4).getMonthValue(), threeMonthsAgo.plusDays(4).getDayOfMonth(), 18, 0), 90, "COMPLETED", "Buổi 2: Tính toán mol và cân bằng");
        sessionSeeder.createSession(completedCourse5, LocalDateTime.of(threeMonthsAgo.plusWeeks(1).plusDays(3).getYear(), threeMonthsAgo.plusWeeks(1).plusDays(3).getMonthValue(), threeMonthsAgo.plusWeeks(1).plusDays(3).getDayOfMonth(), 18, 0), 90, "COMPLETED", "Buổi 3: Ôn tập kiến thức trọng tâm");
        sessionSeeder.createSession(completedCourse6, LocalDateTime.of(threeMonthsAgo.getYear(), threeMonthsAgo.getMonthValue(), threeMonthsAgo.getDayOfMonth(), 17, 0), 90, "COMPLETED", "Buổi 1: Các thì cơ bản");
        sessionSeeder.createSession(completedCourse6, LocalDateTime.of(threeMonthsAgo.plusDays(4).getYear(), threeMonthsAgo.plusDays(4).getMonthValue(), threeMonthsAgo.plusDays(4).getDayOfMonth(), 17, 0), 90, "COMPLETED", "Buổi 2: Câu điều kiện và mệnh đề quan hệ");
        sessionSeeder.createSession(completedCourse6, LocalDateTime.of(threeMonthsAgo.plusWeeks(1).plusDays(3).getYear(), threeMonthsAgo.plusWeeks(1).plusDays(3).getMonthValue(), threeMonthsAgo.plusWeeks(1).plusDays(3).getDayOfMonth(), 17, 0), 90, "COMPLETED", "Buổi 3: Bài tập tổng hợp ngữ pháp");

        feedbackSeeder.createFeedback(completedCourse1, student1, 5, "Gia sư dạy rất dễ hiểu, lộ trình rõ ràng, em tiến bộ rõ rệt.");
        feedbackSeeder.createFeedback(completedCourse2, student2, 5, "Cô dạy rất tận tâm, bài giảng logic và dễ áp dụng khi làm đề.");
        feedbackSeeder.createFeedback(completedCourse3, student3, 4, "Thầy dạy chắc kiến thức, sửa bài kỹ, giúp em tự tin hơn khi làm bài.");

        Chat chat1 = chatSeeder.createChat(user4, user1);
        Chat chat2 = chatSeeder.createChat(user5, user2);
        Chat chat3 = chatSeeder.createChat(user6, user3);
        Chat chat4 = chatSeeder.createChat(user5, user1);
        Chat chat5 = chatSeeder.createChat(user4, user3);
        Chat chat6 = chatSeeder.createChat(user6, user2);

        messageSeeder.createMessage(chat1, user1, "Chào Thu Hà, hôm nay chúng ta sẽ ôn tập Toán nâng cao.", null, false, null);
        messageSeeder.createMessage(chat1, user1, "", "attachment_1000000000001.pdf", false, null);
        messageSeeder.createMessage(chat1, user4, "Dạ, thầy An ơi, em đã chuẩn bị bài tập rồi.", null, false, null);
        messageSeeder.createMessage(chat2, user2, "Quân ơi, hôm nay chúng ta học Lý - Hóa nhé.", null, false, null);
        messageSeeder.createMessage(chat2, user2, "", "attachment_1000000000001.pdf", false, null);
        messageSeeder.createMessage(chat2, user5, "Vâng ạ, em đã đọc trước lý thuyết rồi.", null, false, null);
        messageSeeder.createMessage(chat3, user3, "Lan, hôm nay luyện kỹ năng nghe nói Tiếng Anh.", null, false, null);
        messageSeeder.createMessage(chat3, user3, "", "attachment_1000000000001.pdf", false, null);
        messageSeeder.createMessage(chat3, user6, "Vâng ạ.", null, false, null);
        messageSeeder.createMessage(chat4, user1, "Chào Quân, hôm nay chúng ta học Toán cơ bản nhé.", null, false, null);
        messageSeeder.createMessage(chat4, user1, "", "attachment_1000000000001.pdf", false, null);
        messageSeeder.createMessage(chat4, user5, "Dạ, em đã chuẩn bị bài tập.", null, false, null);
        messageSeeder.createMessage(chat5, user3, "Chào Thu Hà, hôm nay luyện Tiếng Anh giao tiếp.", null, false, null);
        messageSeeder.createMessage(chat5, user3, "", "attachment_1000000000001.pdf", false, null);
        messageSeeder.createMessage(chat5, user4, "Em sẵn sàng ạ.", null, false, null);
        messageSeeder.createMessage(chat6, user2, "Lan, hôm nay học Hóa nâng cao nhé.", null, false, null);
        messageSeeder.createMessage(chat6, user2, "", "attachment_1000000000001.pdf", false, null);
        messageSeeder.createMessage(chat6, user6, "Vâng, em đã chuẩn bị lý thuyết.", null, false, null);

        log.info("Hoàn tất khởi tạo dữ liệu mẫu");
    }
}
