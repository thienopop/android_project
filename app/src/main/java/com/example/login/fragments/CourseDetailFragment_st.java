package com.example.login.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.login.R;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.DetailCourse;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetailFragment_st extends Fragment {

    private static final String ARG_COURSE_ID = "course_id";
    private static final String ARG_TUTOR_ID = "tutor_id";

    private ApiService apiService;
    private int courseId;
    private int tutorId;

    private TextView textFullName;
    private TextView textSubject;
    private TextView textTotalSessions;
    private TextView textCompleteSessions;
    private TextView textTotalPrice;
    private TextView textStartDate;
    private TextView textEndDate;
    private TextView textTimeOfTheLesson;
    private TextView textStatus;
    private TextView textNotes;
    private Button btnRegisterCourse;
    private Button btnViewTutorProfile;
    private ImageButton btnBack;

    public static CourseDetailFragment_st newInstance(int courseId, int tutorId) {
        CourseDetailFragment_st fragment = new CourseDetailFragment_st();
        Bundle args = new Bundle();
        args.putInt(ARG_COURSE_ID, courseId);
        args.putInt(ARG_TUTOR_ID, tutorId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_detail_st, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);

        if (getArguments() != null) {
            courseId = getArguments().getInt(ARG_COURSE_ID);
            tutorId = getArguments().getInt(ARG_TUTOR_ID);
            loadCourseDetail();
        }

        setupListeners();
    }

    private void initViews(View view) {
        textFullName = view.findViewById(R.id.text_full_name);
        textSubject = view.findViewById(R.id.text_subject);
        textTotalSessions = view.findViewById(R.id.text_total_sessions);
        textCompleteSessions = view.findViewById(R.id.text_complete_sessions);
        textTotalPrice = view.findViewById(R.id.text_total_price);
        textStartDate = view.findViewById(R.id.text_start_date);
        textEndDate = view.findViewById(R.id.text_end_date);
        textTimeOfTheLesson = view.findViewById(R.id.text_timeOfTheLesson);
        textStatus = view.findViewById(R.id.text_status);
        textNotes = view.findViewById(R.id.text_notes);
        btnRegisterCourse = view.findViewById(R.id.btn_register_course);
        btnViewTutorProfile = view.findViewById(R.id.btn_view_tutor_profile);
        btnBack = view.findViewById(R.id.btn_back);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            }
        });

        btnRegisterCourse.setOnClickListener(v -> registerCourse());

        btnViewTutorProfile.setOnClickListener(v -> {
            if (tutorId > 0) {
                navigateToTutorDetail(tutorId);
            }
        });
    }

    private void loadCourseDetail() {
        apiService.getDetailCourseByStudent(courseId).enqueue(new Callback<DetailCourse>() {
            @Override
            public void onResponse(Call<DetailCourse> call, Response<DetailCourse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayCourseDetail(response.body());
                } else {
                    Toast.makeText(getContext(), "Không thể tải thông tin khóa học", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DetailCourse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayCourseDetail(DetailCourse course) {
        textFullName.setText(course.getFullName() != null ? course.getFullName() : "N/A");
        textSubject.setText(course.getSubject() != null ? course.getSubject() : "N/A");
        textTotalSessions.setText(String.valueOf(course.getTotalSessions()));
        textCompleteSessions.setText(String.valueOf(course.getCompletedSessions()));
        textTotalPrice.setText(formatCurrency(course.getTotalPrice()));
        textStartDate.setText(formatDate(course.getStartDate()));
        textEndDate.setText(formatDate(course.getEndDate()));
        textTimeOfTheLesson.setText(course.getTimeOfTheLesson() != null ? course.getTimeOfTheLesson() : "N/A");
        textStatus.setText(course.getStatus() != null ? course.getStatus() : "N/A");
        textNotes.setText(course.getNotes() != null ? course.getNotes() : "Không có ghi chú");
    }

    private void registerCourse() {
        apiService.registerCourse(courseId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đăng ký khóa học thành công", Toast.LENGTH_SHORT).show();
                    navigateToExplore();
                } else {
                    Toast.makeText(getContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToTutorDetail(int tutorId) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main_container, TutorDetailFragment_st.newInstance(tutorId))
                .addToBackStack(null)
                .commit();
    }

    private void navigateToExplore() {
        getParentFragmentManager().popBackStack();
    }

    private String formatCurrency(double amount) {
        return NumberFormat.getInstance(new Locale("vi", "VN")).format(amount) + "đ";
    }

    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "N/A";
        }

        try {
            if (dateStr.contains("T")) {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = inputFormat.parse(dateStr);
                return date != null ? outputFormat.format(date) : dateStr;
            } else if (dateStr.contains(" ")) {
                return dateStr.split(" ")[0];
            } else {
                return dateStr;
            }
        } catch (Exception e) {
            if (dateStr.length() >= 10) {
                return dateStr.substring(0, 10);
            }
            return dateStr;
        }
    }
}