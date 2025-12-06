package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.Course;
import com.example.login.model.CourseInfo;
import com.example.login.model.DetailCourse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import android.view.View;
import android.widget.*;

public class UpdateCourseActivity extends AppCompatActivity {


    EditText edtNotes, edtTimeOfTheLesson,edtTotalPrice, edtTotalSessions,edtSubject;
    Button btnUpdateCourse,btnCancel;
    ImageButton btn_back;
    private int course_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_course);

        // Initialize Views
        edtNotes=findViewById(R.id.edtNotes);
        edtTimeOfTheLesson=findViewById(R.id.edtTimeOfTheLesson);
        edtTotalPrice=findViewById(R.id.edtTotalPrice);
        edtTotalSessions=findViewById(R.id.edtTotalSessions);
        edtSubject =findViewById(R.id.edtSubject);

        btnUpdateCourse =findViewById(R.id.btnUpdateCourse);
                btnCancel=findViewById(R.id. btnCancel);
        btn_back=findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng Activity hiện tại và quay lại Fragment/Activity trước đó
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng Activity hiện tại và quay lại Fragment/Activity trước đó
                finish();
            }
        });
        // --- 3. TẢI DỮ LIỆU ---
        Intent intent = getIntent();
        if (intent != null) {
            course_id = intent.getIntExtra("COURSE_ID", -1);
            if (course_id != -1) {
                // Tải chi tiết khóa học
                loadCourse(course_id);
            } else {
                Toast.makeText(this, "Không tìm thấy ID khóa học.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void updateCourse()
    {

        String subject = edtSubject.getText().toString().trim();
        String notes = edtNotes.getText().toString().trim();
        String timeOfTheLesson = edtTimeOfTheLesson.getText().toString().trim();
        String totalPriceString = edtTotalPrice.getText().toString().trim();
        String totalSessionsString = edtTotalSessions.getText().toString().trim();

        if (subject.isEmpty() || notes.isEmpty() || timeOfTheLesson.isEmpty() ||
                totalPriceString.isEmpty() || totalSessionsString.isEmpty()) {
            Toast.makeText(UpdateCourseActivity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }
        int totalPrice;
        int totalSessions;
        try {
            totalPrice = Integer.parseInt(totalPriceString);
            totalSessions = Integer.parseInt(totalSessionsString);
        } catch (NumberFormatException e) {
            Toast.makeText(UpdateCourseActivity.this, "Giá tiền và Tổng số buổi phải là số!", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);

        Course newCourse = new Course(subject, notes, timeOfTheLesson, totalPrice, totalSessions);
        newCourse.setId(course_id);
        // SỬA LỖI 1: Phải là Call<Void>
        // (Giả sử ApiService.AddCourse đã trả về Call<Void>)
        Call<CourseInfo> call = apiService.UpdateCourse(newCourse);

        // Xử lý Callback<Void>
        call.enqueue(new Callback<CourseInfo>() {
            @Override
            // SỬA LỖI 2: Tham số phải là Call<Void>
            public void onResponse(Call<CourseInfo> call, Response<CourseInfo> response) {

                // SỬA LỖI 4: Xóa kiểm tra response.body()
                if (response.isSuccessful()) {

                    Toast.makeText(UpdateCourseActivity.this, "Cập nhật khoá học thành công!", Toast.LENGTH_SHORT).show();

////                        chuyển đến thêm khoá học
//                    CourseInfo course = response.body();
//                    Intent intent = new Intent(requireContext(), DetailCourseActivity.class);
//                    int courseId = course.getId();
//
//                    intent.putExtra("COURSE_ID_KEY", courseId);
////                    startActivity(intent);
//
//                    if (getActivity() != null) {
//                        getActivity().getSupportFragmentManager().popBackStack();
//                    }


                } else {
                    // Server trả về lỗi (4xx, 5xx)
                    Toast.makeText(UpdateCourseActivity.this, "Thêm thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            // SỬA LỖI 3: Tham số phải là Call<Void>
            public void onFailure(Call<CourseInfo> call, Throwable t) {
                // Lỗi mạng hoặc kết nối
                // SỬA LỖI 5: Xóa setText không cần thiết
                Toast.makeText(UpdateCourseActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadCourse(int id) {
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<DetailCourse> call = apiService.getDetailCourseByTutor(id);

        call.enqueue(new Callback<DetailCourse>() {
            @Override
            public void onResponse(@NonNull Call<DetailCourse> call, @NonNull Response<DetailCourse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DetailCourse course = response.body();
                    displayCourse(course);
                } else {
                    Toast.makeText(UpdateCourseActivity.this, "Không thể tải chi tiết khóa học.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DetailCourse> call, @NonNull Throwable t) {
                Toast.makeText(UpdateCourseActivity.this, "Lỗi mạng. Vui lòng kiểm tra kết nối.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void displayCourse(DetailCourse course)
    {

        edtSubject.setText(course.getSubject());
        edtNotes.setText(course.getNotes());
        edtTimeOfTheLesson.setText(course.getTimeOfTheLesson());
        edtTotalPrice.setText(String.valueOf(course.getTotalPrice()));
        edtTotalSessions.setText(String.valueOf(course.getTotalSessions()));

    }


}