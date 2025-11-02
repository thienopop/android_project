package com.example.demo.config.seeder;

import com.example.demo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Seeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Seeder.class);

    @Autowired private UserSeeder userSeeder;
    @Autowired private TutorSeeder tutorSeeder;
    @Autowired private StudentSeeder studentSeeder;

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

        userSeeder.createUser("quantri1", "quantri1@vidu.com", "matkhau123", "ADMIN");
        userSeeder.createUser("quantri2", "quantri2@vidu.com", "matkhau123", "ADMIN");
        userSeeder.createUser("quantri3", "quantri3@vidu.com", "matkhau123", "ADMIN");

        User giaSu1 = userSeeder.createUser("giasu1", "giasu1@vidu.com", "matkhau123", "TUTOR");
        User giaSu2 = userSeeder.createUser("giasu2", "giasu2@vidu.com", "matkhau123", "TUTOR");
        User giaSu3 = userSeeder.createUser("giasu3", "giasu3@vidu.com", "matkhau123", "TUTOR");

        User hs1 = userSeeder.createUser("hocsinh1", "hocsinh1@vidu.com", "matkhau123", "STUDENT");
        User hs2 = userSeeder.createUser("hocsinh2", "hocsinh2@vidu.com", "matkhau123", "STUDENT");
        User hs3 = userSeeder.createUser("hocsinh3", "hocsinh3@vidu.com", "matkhau123", "STUDENT");

        tutorSeeder.createTutor(giaSu1, "Nguyễn Văn An", "0901234567", "123 Đường Lê Lợi, TP.HCM",
                LocalDate.of(1985, 5, 15), "Gia sư Toán với hơn 10 năm kinh nghiệm dạy kèm học sinh cấp 3",
                10, 300000.0, true, 150, 4.8, "anh1.jpg");

        tutorSeeder.createTutor(giaSu2, "Trần Thị Bình", "0907654321", "456 Nguyễn Trãi, Hà Nội",
                LocalDate.of(1990, 8, 22), "Chuyên dạy Lý - Hóa, giúp học sinh hiểu sâu bản chất",
                7, 250000.0, true, 120, 4.7, "anh2.jpg");

        tutorSeeder.createTutor(giaSu3, "Lê Hoàng Minh", "0903456789", "789 Võ Văn Kiệt, Đà Nẵng",
                LocalDate.of(1988, 3, 10), "Gia sư tiếng Anh, luyện thi đại học và IELTS",
                8, 350000.0, false, 80, 4.5, "anh3.jpg");

        studentSeeder.createStudent(hs1, "Phạm Thu Hà", "0911234567", "321 Lý Thường Kiệt, TP.HCM",
                LocalDate.of(2008, 6, 20), "Lớp 10", "Yêu thích các môn Khoa học Tự nhiên");

        studentSeeder.createStudent(hs2, "Vũ Minh Quân", "0919876543", "654 Hai Bà Trưng, Hà Nội",
                LocalDate.of(2009, 9, 14), "Lớp 9", "Muốn cải thiện điểm Toán và Tiếng Anh");

        studentSeeder.createStudent(hs3, "Đỗ Ngọc Lan", "0912345678", "987 Nguyễn Huệ, Huế",
                LocalDate.of(2007, 12, 5), "Lớp 11", "Đang chuẩn bị cho kỳ thi đại học");

        log.info("Hoàn tất khởi tạo dữ liệu mẫu");
    }
}
