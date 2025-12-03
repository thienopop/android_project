package com.example.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
//         android:id="@+id/showAddCourse"
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.example.login.api.ApiService;
import com.example.login.api.PrefsHelper;
import com.example.login.api.RetrofitClient;
import com.example.login.model.ChatWithUserDetail;
import com.example.login.model.CourseInfo;
import com.example.login.model.User;
import com.example.login.model.RegisterLoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.annotation.NonNull;


import com.example.login.R;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.Course;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import com.example.login.model.AddSession;
import java.util.Calendar;
import com.example.login.adapter.ListSessionOfCourseAdapter;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.DetailCourse;
import com.example.login.model.SessionInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class DetailCourseActivity extends AppCompatActivity {

    // Khai báo biến
    LinearLayout showAddCourse;
    ImageButton btnBack,imShowAddCourse,im_message;
    ListView listViewSessions;
    TextView text_timeOfTheLesson, text_complete_sessions, text_notes, text_status,
            text_end_date, text_start_date, text_total_price, text_total_sessions,
            text_subject, text_full_name;

    EditText tvDate, edtNotes,edtDuration;
    EditText tvTime;
    Button btCancelAddSession;
    Button btAddSession;
    int courseId=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_course);

        // --- 1. ÁNH XẠ VIEW (FIND VIEWS) ---
        edtNotes=findViewById(R.id.edtNotes);
        edtDuration=findViewById(R.id.edtDuration);
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

        btCancelAddSession=findViewById(R.id.btCancelAddSession);
        btAddSession=findViewById(R.id.btAddSession);
        imShowAddCourse=findViewById(R.id.imShowAddCourse);
        showAddCourse=findViewById(R.id.showAddCourse);

        im_message=findViewById(R.id.im_message);



        im_message.setOnClickListener(v -> {

            String value = v.getTag().toString();

            // Chuyển String → int
            int idChat = Integer.parseInt(value);
            String fragment_name="FRAGMENT_CHAT";
            Intent intent = new Intent(DetailCourseActivity.this, TutorDashboardActivity.class);
            intent.putExtra("FRAGMENT_NAME", fragment_name);
            intent.putExtra("CHAT_ID", idChat);

            startActivity(intent);
        });


//
//        CourseInfo course = courseList.get(position);
//
//        Intent intent = new Intent(requireContext(), DetailCourseActivity.class);
//        int courseId = course.getId();
//
//        intent.putExtra("COURSE_ID_KEY", courseId);
//        startActivity(intent);
//
        btAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSession();
            }
        });

        // --- 2. THIẾT LẬP NÚT BACK ---
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng Activity hiện tại và quay lại Fragment/Activity trước đó
                finish();
            }
        });

        imShowAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCourse.setVisibility(View.VISIBLE);
            }
        });

        btCancelAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCourse.setVisibility(View.GONE);
            }
        });

        // --- 3. TẢI DỮ LIỆU ---
        Intent intent = getIntent();
        if (intent != null) {
            courseId = intent.getIntExtra("COURSE_ID_KEY", -1);
            if (courseId != -1) {
                // Tải chi tiết khóa học
                loadCourseDetails(courseId);
                // Tải danh sách các phiên (sessions)
                loadSessionsOfCourse(courseId); // Thêm hàm tải session
            } else {
                Toast.makeText(this, "Không tìm thấy ID khóa học.", Toast.LENGTH_SHORT).show();
            }
        }

        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);

        // --- Chọn ngày ---
        tvDate.setOnClickListener(v -> showDatePicker());

        // --- Chọn giờ ---
        tvTime.setOnClickListener(v -> showTimePicker());
    }
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                DetailCourseActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    tvDate.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                DetailCourseActivity.this,
                (view, hourOfDay, minuteOfHour) -> {
                    String selectedTime = hourOfDay + ":" + (minuteOfHour < 10 ? "0" + minuteOfHour : minuteOfHour);
                    tvTime.setText(selectedTime);
                },
                hour, minute, true // 24h format
        );

        timePickerDialog.show();
    }


    private void loadCourseDetails(int id) {
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<DetailCourse> call = apiService.getDetailCourseByTutor(id);

        call.enqueue(new Callback<DetailCourse>() {
            @Override
            public void onResponse(@NonNull Call<DetailCourse> call, @NonNull Response<DetailCourse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DetailCourse detail = response.body();
                    displayCourseDetails(detail);
                } else {
                    Toast.makeText(DetailCourseActivity.this, "Không thể tải chi tiết khóa học.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DetailCourse> call, @NonNull Throwable t) {
                Toast.makeText(DetailCourseActivity.this, "Lỗi mạng. Vui lòng kiểm tra kết nối.", Toast.LENGTH_SHORT).show();
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
        im_message.setTag(course.getUserId());


        text_full_name.setText(course.getFullName());
        text_end_date.setText(course.getEndDate());
        text_start_date.setText(course.getStartDate());
        text_status.setText(course.getStatus());

        text_notes.setText(course.getNotes() != null && !course.getNotes().isEmpty() ? course.getNotes() : "Không có ghi chú.");
    }

    private void addSession() {

        // Lấy dữ liệu từ giao diện
        String dateStr = tvDate.getText().toString().trim();   // 25/11/2025
        String timeStr = tvTime.getText().toString().trim();   // 14:30
        String notes = edtNotes.getText().toString().trim();
        String durationStr = edtDuration.getText().toString().trim();

        // ===== Validate =====
        if (dateStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (timeStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn giờ!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (notes.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập ghi chú!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (durationStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập thời lượng!", Toast.LENGTH_SHORT).show();
            return;
        }

        int duration = 0;
        try {
            duration = Integer.parseInt(durationStr);
        } catch (Exception e) {
            Toast.makeText(this, "Thời lượng phải là số!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (duration <= 0) {
            Toast.makeText(this, "Thời lượng phải lớn hơn 0!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (courseId == 0) {
            Toast.makeText(this, "Thiếu Course ID!", Toast.LENGTH_SHORT).show();
            return;
        }

        // ===== Convert date + time → datetime =====
        String[] dateParts = dateStr.split("/");
        if (dateParts.length != 3) {
            Toast.makeText(this, "Sai định dạng ngày!", Toast.LENGTH_SHORT).show();
            return;
        }

        String day = dateParts[0];
        String month = dateParts[1];
        String year = dateParts[2];

//      Thêm cả giây (2025-11-02T09:00:00)
        String dateTime = year + "-" + month + "-" + day + "T" + timeStr + ":00";
// Kết quả: "2025-11-02T09:00:00"
        // Tạo object gửi api
        AddSession addSession = new AddSession(duration, notes, dateTime, courseId);

        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<Void> call = apiService.addSession(addSession);

        // gọi api
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(DetailCourseActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailCourseActivity.this, "Thêm thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DetailCourseActivity.this, "Không thể kết nối server!", Toast.LENGTH_SHORT).show();
            }
        });
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
                    ListSessionOfCourseAdapter adapter = new ListSessionOfCourseAdapter(DetailCourseActivity.this, sessionList);
                    listViewSessions.setAdapter(adapter);

                    listViewSessions.setOnItemClickListener((parent, view, position, id) -> {
                        SessionInfo ss = sessionList.get(position);
                        Toast.makeText(DetailCourseActivity.this, "Bạn chọn session ID: " + ss.getId(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    listViewSessions.setAdapter(null);
                    Toast.makeText(DetailCourseActivity.this, "Không có lịch học cho khóa này.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SessionInfo>> call, @NonNull Throwable t) {
                Toast.makeText(DetailCourseActivity.this, "Lỗi API tải sessions: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



//
//    private void loadChats() {
//        ApiService api = RetrofitClient.getClient(this).create(ApiService.class);
//        api.getMyChats().enqueue(new Callback<List<ChatWithUserDetail>>() {
//            @Override
//            public void onResponse(Call<List<ChatWithUserDetail>> call, Response<List<ChatWithUserDetail>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    adapter.setItems(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<ChatWithUserDetail>> call, Throwable t) {
//            }
//        });
//    }




}