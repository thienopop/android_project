package com.example.login;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.adapter.ListSessionOfCourseAdapter;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//         android:id="@+id/showAddCourse"


import com.example.login.model.AddSession;
import com.example.login.model.DetailCourse;
import com.example.login.model.SessionInfo;

public class DetailCourseActivity_admin extends AppCompatActivity {

    // Khai báo biến
    LinearLayout showAddCourse;
    ImageButton btnBack,imShowAddCourse,im_message;
    ListView listViewSessions;
    TextView text_timeOfTheLesson, text_complete_sessions, text_notes, text_status,
            text_end_date, text_start_date, text_total_price, text_total_sessions,
            text_subject, text_full_name ,update_course;

    EditText tvDate, edtNotes,edtDuration;
    EditText tvTime;
    Button    btCancelCreateCourse, btConfimAddCourse;

    int courseId=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_course_admin);
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

        btCancelCreateCourse=findViewById(R.id.btCancelCreateCourse);
        btConfimAddCourse=findViewById(R.id.btConfimAddCourse);
        im_message=findViewById(R.id.im_message);

        update_course=findViewById(R.id.update_course);

        text_status.setOnClickListener(v -> {
            updateCourseStatus(text_status.getText().toString());

        });
//chuyển đến trang cập nhật khoá hcoj
        update_course.setOnClickListener(v -> {

            Intent intent = new Intent(com.example.login.DetailCourseActivity_admin.this, UpdateCourseActivity.class);
            intent.putExtra("COURSE_ID", courseId);
            startActivity(intent);
        });

        im_message.setOnClickListener(v -> {

            String value = v.getTag().toString();

            // Chuyển String → int
            int idChat = Integer.parseInt(value);
            String fragment_name="FRAGMENT_CHAT";
            Intent intent = new Intent(com.example.login.DetailCourseActivity_admin.this, TutorDashboardActivity.class);
            intent.putExtra("FRAGMENT_NAME", fragment_name);
            intent.putExtra("CHAT_ID", idChat);

            startActivity(intent);
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng Activity hiện tại và quay lại Fragment/Activity trước đó
                finish();
            }
        });

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

    }
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                com.example.login.DetailCourseActivity_admin.this,
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
                com.example.login.DetailCourseActivity_admin.this,
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
                    Toast.makeText(com.example.login.DetailCourseActivity_admin.this, "Không thể tải chi tiết khóa học.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<DetailCourse> call, @NonNull Throwable t) {
                Toast.makeText(com.example.login.DetailCourseActivity_admin.this, "Lỗi mạng. Vui lòng kiểm tra kết nối.", Toast.LENGTH_SHORT).show();
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
    private void loadSessionsOfCourse(int id) {
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<List<SessionInfo>> call = apiService.getSessionsByCourseId(id);

        call.enqueue(new Callback<List<SessionInfo>>() {
            @Override
            public void onResponse(@NonNull Call<List<SessionInfo>> call, @NonNull Response<List<SessionInfo>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<SessionInfo> sessionList = response.body();
                    // SỬA LỖI: Dùng DetailCourseActivity.this thay vì getContext()
                    ListSessionOfCourseAdapter adapter = new ListSessionOfCourseAdapter(com.example.login.DetailCourseActivity_admin.this, sessionList);
                    listViewSessions.setAdapter(adapter);

                    listViewSessions.setOnItemClickListener((parent, view, position, id) -> {
                        SessionInfo ss = sessionList.get(position);
                        Toast.makeText(com.example.login.DetailCourseActivity_admin.this, "Bạn chọn session ID: " + ss.getId(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    listViewSessions.setAdapter(null);
                    Toast.makeText(com.example.login.DetailCourseActivity_admin.this, "Không có lịch học cho khóa này.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<SessionInfo>> call, @NonNull Throwable t) {
                Toast.makeText(com.example.login.DetailCourseActivity_admin.this, "Lỗi API tải sessions: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
    private void updateCourseStatus(String status) {

        String nextStatus="";
        String text="";
        if(status.equals("STUDENT_REGISTERED"))
        {
            text="Bắt đầu khoá học";
            nextStatus="ONGOING";
        }else if (status.equals("ONGOING"))
        {
            text="Hoàn thành khoá học";
            nextStatus="COMPLETED";
        }
//            "NEW", "STUDENT_REGISTERED", "ONGOING", "COMPLETED", "CANCELLED");
//        Dialog dialog = new Dialog(requireContext()
        //        );
        Dialog dialog = new Dialog(com.example.login.DetailCourseActivity_admin.this);

        dialog.setContentView(R.layout.show_to_update_status_course_card);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);

// Ánh xạ view
        TextView txtConten = dialog.findViewById(R.id.txtConten);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

// Set dữ liệu
        txtConten.setText(text);

// Sự kiện nút Cancel
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        final String finalStatus = nextStatus;
// Sự kiện nút Confirm
//        btnConfirm.setOnClickListener(v -> {
//
//            updateCsourseStatus(finalStatus);
//            dialog.dismiss();
//        });

// Hiện dialog CUỐI CÙNG
        dialog.show();

    }
//
//    private void updateCsourseStatus(String status) {
//
//        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
//        Call<Void> call = apiService.updateCourseStatus(courseId, status);
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
//
//                if (response.isSuccessful()) {
//                    Toast.makeText(com.example.login.DetailCourseActivity_admin.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(com.example.login.DetailCourseActivity_admin.this, "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
//                Toast.makeText(com.example.login.DetailCourseActivity_admin.this, "Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}