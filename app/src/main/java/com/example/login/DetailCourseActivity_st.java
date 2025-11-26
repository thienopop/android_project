package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.adapter.ListSessionOfCourseAdapter;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.DetailCourse;
import com.example.login.model.SessionInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailCourseActivity_st extends AppCompatActivity {

    // Khai báo biến
    ImageButton btnBack;
    ListView listViewSessions;
    TextView text_timeOfTheLesson, text_complete_sessions, text_notes, text_status,
            text_end_date, text_start_date, text_total_price, text_total_sessions,
            text_subject, text_full_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_course_st);

        // --- 1. ÁNH XẠ VIEW (FIND VIEWS) ---
        text_timeOfTheLesson = findViewById(R.id.text_timeOfTheLesson);
        text_complete_sessions = findViewById(R.id.text_complete_sessions);
        text_notes = findViewById(R.id.text_notes);
        text_status = findViewById(R.id.text_status);
        text_end_date = findViewById(R.id.text_end_date);
        text_start_date = findViewById(R.id.text_start_date);
        text_total_price = findViewById(R.id.text_total_price);
        text_total_sessions = findViewById(R.id.text_total_sessions);
        text_subject = findViewById(R.id.text_subject);
        text_full_name = findViewById(R.id.text_full_name);

        // SỬA LỖI: Ánh xạ ListView đúng cách
        listViewSessions = findViewById(R.id.listViewSessions);

        btnBack = findViewById(R.id.btn_back);

        // --- 2. THIẾT LẬP NÚT BACK ---
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng Activity hiện tại và quay lại Fragment/Activity trước đó
                finish();
            }
        });

        // --- 3. TẢI DỮ LIỆU ---
        Intent intent = getIntent();
        if (intent != null) {
            int courseId = intent.getIntExtra("COURSE_ID_KEY", -1);
            if (courseId != -1) {
                // Tải chi tiết khóa học
                loadCourseDetails(courseId);
                // Tải danh sách các phiên (sessions)
                loadSessionsOfCourse(courseId); // Thêm hàm tải session
            } else {
                Toast.makeText(this, "Không tìm thấy ID khóa học.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void loadCourseDetails(int id) {
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<DetailCourse> call = apiService.getDetailCourseByStudent(id);

        call.enqueue(new Callback<DetailCourse>() {
            @Override
            public void onResponse(@NonNull Call<DetailCourse> call, @NonNull Response<DetailCourse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DetailCourse detail = response.body();
                    displayCourseDetails(detail);
                } else {
                    Toast.makeText(DetailCourseActivity_st.this, "Không thể tải chi tiết khóa học.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DetailCourse> call, @NonNull Throwable t) {
                Toast.makeText(DetailCourseActivity_st.this, "Lỗi mạng. Vui lòng kiểm tra kết nối.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayCourseDetails(DetailCourse course) {
        if (course == null) return;

        text_timeOfTheLesson.setText(course.getTimeOfTheLesson());
        text_subject.setText(course.getSubject());

        text_complete_sessions.setText(String.valueOf(course.getCompletedSessions()));
        text_total_sessions.setText(String.valueOf(course.getTotalSessions()));

        String formattedPrice = String.format("%,.0f VND", course.getTotalPrice());
        text_total_price.setText(formattedPrice);

        text_full_name.setText(course.getFullName());
        text_end_date.setText(course.getEndDate());
        text_start_date.setText(course.getStartDate());
        text_status.setText(course.getStatus());

        text_notes.setText(course.getNotes() != null && !course.getNotes().isEmpty() ? course.getNotes() : "Không có ghi chú.");
    }


    private void loadSessionsOfCourse(int id) {
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<List<SessionInfo>> call = apiService.getSessionsByCourseId(id);

        call.enqueue(new Callback<List<SessionInfo>>() {
            @Override
            public void onResponse(@NonNull Call<List<SessionInfo>> call, @NonNull Response<List<SessionInfo>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<SessionInfo> sessionList = response.body();
                    // SỬA LỖI: Dùng DetailCourseActivity.this thay vì getContext()
                    ListSessionOfCourseAdapter adapter = new ListSessionOfCourseAdapter(DetailCourseActivity_st.this, sessionList);
                    listViewSessions.setAdapter(adapter);

                    listViewSessions.setOnItemClickListener((parent, view, position, id) -> {
                        SessionInfo ss = sessionList.get(position);
                        Toast.makeText(DetailCourseActivity_st.this, "Bạn chọn session ID: " + ss.getId(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    listViewSessions.setAdapter(null);
                    Toast.makeText(DetailCourseActivity_st.this, "Không có lịch học cho khóa này.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SessionInfo>> call, @NonNull Throwable t) {
                Toast.makeText(DetailCourseActivity_st.this, "Lỗi API tải sessions: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}