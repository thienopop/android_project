package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.adapter.ListSessionOfCourseAdapter;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.Course;
import com.example.login.model.DetailCourse;
import com.example.login.model.Feedback;
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
    TextView textRatingTitle;
    LinearLayout layoutRatingInput, layoutRatingDisplay;
    RatingBar ratingBar, ratingBarDisplay;
    EditText editComment;
    Button btnSubmitRating;
    TextView textRatingValue, textCommentDisplay;
    int currentCourseId;
    String currentCourseStatus;

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

        textRatingTitle = findViewById(R.id.text_rating_title);
        layoutRatingInput = findViewById(R.id.layout_rating_input);
        layoutRatingDisplay = findViewById(R.id.layout_rating_display);
        ratingBar = findViewById(R.id.rating_bar);
        ratingBarDisplay = findViewById(R.id.rating_bar_display);
        editComment = findViewById(R.id.edit_comment);
        btnSubmitRating = findViewById(R.id.btn_submit_rating);
        textRatingValue = findViewById(R.id.text_rating_value);
        textCommentDisplay = findViewById(R.id.text_comment_display);

        btnSubmitRating.setOnClickListener(v -> submitFeedback());

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
            currentCourseId = courseId;
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

        currentCourseStatus = course.getStatus();

        if ("COMPLETED".equals(currentCourseStatus)) {
            textRatingTitle.setVisibility(View.VISIBLE);
            loadFeedback(currentCourseId);
        } else {
            textRatingTitle.setVisibility(View.GONE);
            layoutRatingInput.setVisibility(View.GONE);
            layoutRatingDisplay.setVisibility(View.GONE);
        }
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

    private void loadFeedback(int courseId) {
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<Feedback> call = apiService.getFeedbackByCourseId(courseId);

        call.enqueue(new Callback<Feedback>() {
            @Override
            public void onResponse(@NonNull Call<Feedback> call, @NonNull Response<Feedback> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayFeedback(response.body());
                } else if (response.code() == 404) {
                    showRatingInput();
                } else {
                    Toast.makeText(DetailCourseActivity_st.this, "Không thể tải đánh giá.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Feedback> call, @NonNull Throwable t) {
                Toast.makeText(DetailCourseActivity_st.this, "Lỗi mạng khi tải đánh giá.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRatingInput() {
        layoutRatingInput.setVisibility(View.VISIBLE);
        layoutRatingDisplay.setVisibility(View.GONE);
    }

    private void displayFeedback(Feedback feedback) {
        layoutRatingInput.setVisibility(View.GONE);
        layoutRatingDisplay.setVisibility(View.VISIBLE);

        ratingBarDisplay.setRating(feedback.getRating());
        textRatingValue.setText("(" + feedback.getRating() + "/5)");
        textCommentDisplay.setText(feedback.getComment());
    }

    private void submitFeedback() {
        int rating = (int) ratingBar.getRating();
        String comment = editComment.getText().toString().trim();

        if (comment.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nhận xét.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<Course> call = apiService.getCourseById(currentCourseId);

        call.enqueue(new Callback<Course>() {
            @Override
            public void onResponse(@NonNull Call<Course> call, @NonNull Response<Course> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Course course = response.body();

                    Feedback feedback = new Feedback();
                    feedback.setCourseId(currentCourseId);
                    feedback.setStudentId(course.getStudent_Id());
                    feedback.setRating(rating);
                    feedback.setComment(comment);

                    createFeedback(feedback);
                } else {
                    Toast.makeText(DetailCourseActivity_st.this, "Không thể lấy thông tin khóa học.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Course> call, @NonNull Throwable t) {
                Toast.makeText(DetailCourseActivity_st.this, "Lỗi mạng khi lấy thông tin khóa học.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createFeedback(Feedback feedback) {
        android.util.Log.d("DEBUG_FEEDBACK", "Creating feedback - CourseId: " + feedback.getCourseId()
                + ", StudentId: " + feedback.getStudentId()
                + ", Rating: " + feedback.getRating()
                + ", Comment: " + feedback.getComment());

        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<Void> call = apiService.createFeedback(feedback);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailCourseActivity_st.this, "Gửi đánh giá thành công!", Toast.LENGTH_SHORT).show();
                    loadFeedback(currentCourseId);
                } else {
                    Toast.makeText(DetailCourseActivity_st.this, "Gửi đánh giá thất bại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(DetailCourseActivity_st.this, "Lỗi mạng khi gửi đánh giá.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}