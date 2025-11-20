package com.example.login;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login.adapter.FragmentsCourseAdapter_st;
import com.example.login.api.ApiService;
import com.example.login.api.PrefsHelper;
import com.example.login.api.RetrofitClient;
import com.example.login.model.CourseInfo;
import com.example.login.model.DetailCourse;
import com.example.login.model.RegisterLoginResponse;
import com.example.login.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

        import com.example.login.api.ApiService;
import com.example.login.api.PrefsHelper;
import com.example.login.api.RetrofitClient;
import com.example.login.model.RegisterLoginResponse;
import com.example.login.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

public class DetailCourseActivity extends AppCompatActivity {



//    ImageButton btnBack = findViewById(R.id.btn_back);


ListView listViewSessions;
    TextView text_timeOfTheLesson,text_complete_sessions, text_notes ,text_status, text_end_date, text_start_date,text_total_price, text_total_sessions, text_subject, text_full_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_course);
        text_timeOfTheLesson=findViewById(R.id.text_timeOfTheLesson);
        text_complete_sessions= findViewById(R.id.text_complete_sessions);
        text_notes= findViewById(R.id.text_notes);
        text_status= findViewById(R.id.text_status);
        text_end_date= findViewById(R.id.text_end_date);
        text_start_date= findViewById(R.id.text_start_date);
        text_total_price= findViewById(R.id.text_total_price);
        text_total_sessions= findViewById(R.id.text_total_sessions);
        text_subject= findViewById(R.id.text_subject);
        text_full_name=findViewById(R.id.text_full_name);

//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Đóng Activity hiện tại và quay lại Activity trước đó
//                finish();
//            }
//        });
        Intent intent = getIntent();

        int courseId;

        if (intent != null) {

             courseId = intent.getIntExtra("COURSE_ID_KEY", -1);

            loadCourseDetails(courseId);
        }







//
//
//
//        // Trong Activity A (Ví dụ: MainActivity)
//
//// 1. Khởi tạo Intent để chuyển sang DetailActivity
//        Intent intent = new Intent(MainActivity.this, DetailCourseActivity.class);
//
//// 2. Dữ liệu cần gửi
//        String userName = "Nguyễn Văn A";
//        int courseId = 101;
//        double totalCost = 5000000.0;
//
//// 3. Đặt dữ liệu vào Intent bằng putExtra(Key, Value)
//// Rất quan trọng: Key phải là chuỗi hằng số (final static String) để tránh lỗi chính tả khi nhận
//        intent.putExtra("USER_NAME_KEY", userName);
//        intent.putExtra("COURSE_ID_KEY", courseId);
//        intent.putExtra("TOTAL_COST_KEY", totalCost);
//
//// 4. Khởi động Activity mới
//        startActivity(intent);




    }


    private void loadCourseDetails(int id) {
        // Thay đổi tên hàm thành loadCourseDetails để rõ ràng hơn
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        // Giả sử API trả về DetailCourse, không phải List<DetailCourse>
        Call<DetailCourse> call = apiService.getDetailCourseByTutor(id);

        call.enqueue(new Callback<DetailCourse>() {
            @Override
            public void onResponse(Call<DetailCourse> call, Response<DetailCourse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    DetailCourse detail = response.body();
                    // 4. HIỂN THỊ DỮ LIỆU
                    displayCourseDetails(detail);
                } else {

//                    Log.e(TAG, "Lỗi API: " + response.code());
                    Toast.makeText(DetailCourseActivity.this, "Không thể tải chi tiết khóa học.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DetailCourse> call, Throwable t) {
//                Log.e(TAG, "Lỗi kết nối: " + t.getMessage(), t);
                Toast.makeText(DetailCourseActivity.this, "Lỗi mạng. Vui lòng kiểm tra kết nối.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayCourseDetails(DetailCourse course) {

        if (course == null) return;

        // --- KHẮC PHỤC LỖI LOGIC: GÁN ĐÚNG GIÁ TRỊ VÀO ĐÚNG TRƯỜNG ---

        // SỬA LỖI LOGIC: Gán Thời gian học (ví dụ: Thứ 3, Thứ 5)
        text_timeOfTheLesson.setText(course.getTimeOfTheLesson());

        // Gán Môn học
        text_subject.setText( course.getSubject());


        // --- GÁN DỮ LIỆU SỐ (INT) -> CHUYỂN THÀNH STRING ---
        // Sửa lỗi biên dịch: setText(int)
        text_complete_sessions.setText(String.valueOf(course.getCompletedSessions()));
        text_total_sessions.setText(String.valueOf(course.getTotalSessions()));

        // --- GÁN DỮ LIỆU DOUBLE -> ĐỊNH DẠNG VÀ CHUYỂN THÀNH STRING ---
        // Sửa lỗi biên dịch: setText(double)
        String formattedPrice = String.format("%,.0f VND", course.getTotalPrice());
        text_total_price.setText(formattedPrice);

        // --- GÁN DỮ LIỆU CHUỖI (STRING) ---
        text_full_name.setText(course.getFullName());
        text_end_date.setText(course.getEndDate());
        text_start_date.setText(course.getStartDate());
        text_status.setText(course.getStatus());

        // Xử lý ghi chú (trường hợp có thể null)
        text_notes.setText(course.getNotes() != null ? course.getNotes() : "Không có ghi chú.");

        // Gán các trường còn lại (startTime, endTime nếu có)
        // Giả định bạn đã khai báo và ánh xạ text_start_time và text_end_time:
        // text_start_time.setText(course.getStartTime());
        // text_end_time.setText(course.getEndTime());
    }
}