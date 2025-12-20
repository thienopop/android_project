package com.example.login.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.login.R;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.DetailCourse;
import com.example.login.model.Tutor;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutorDetailFragment_st extends Fragment {

    private static final String ARG_TUTOR_ID = "tutor_id";

    private ApiService apiService;
    private int tutorId;

    private ImageView imgTutor;
    private TextView textFullName;
    private TextView textAverageRating;
    private TextView textTotalSessions;
    private TextView textExperienceYears;
    private TextView textDateOfBirth;
    private TextView textPhone;
    private TextView textAddress;
    private TextView textBio;
    private LinearLayout layoutCourses;
    private ImageButton btnBack;

    private List<DetailCourse> newCourses = new ArrayList<>();

    public static TutorDetailFragment_st newInstance(int tutorId) {
        TutorDetailFragment_st fragment = new TutorDetailFragment_st();
        Bundle args = new Bundle();
        args.putInt(ARG_TUTOR_ID, tutorId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutor_detail_st, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);

        if (getArguments() != null) {
            tutorId = getArguments().getInt(ARG_TUTOR_ID);
            loadTutorDetail();
            loadTutorCourses();
        }

        setupListeners();
    }

    private void initViews(View view) {
        imgTutor = view.findViewById(R.id.imgTutor);
        textFullName = view.findViewById(R.id.text_full_name);
        textAverageRating = view.findViewById(R.id.text_average_rating);
        textTotalSessions = view.findViewById(R.id.text_total_sessions);
        textExperienceYears = view.findViewById(R.id.text_experience_years);
        textDateOfBirth = view.findViewById(R.id.text_date_of_birth);
        textPhone = view.findViewById(R.id.text_phone);
        textAddress = view.findViewById(R.id.text_address);
        textBio = view.findViewById(R.id.text_bio);
        layoutCourses = view.findViewById(R.id.layoutCourses);
        btnBack = view.findViewById(R.id.btn_back);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            }
        });
    }

    private void loadTutorDetail() {
        apiService.getTutorById(tutorId).enqueue(new Callback<Tutor>() {
            @Override
            public void onResponse(Call<Tutor> call, Response<Tutor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayTutorDetail(response.body());
                } else {
                    Toast.makeText(getContext(), "Không thể tải thông tin gia sư", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tutor> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTutorCourses() {
        apiService.getCoursesByTutorId(tutorId).enqueue(new Callback<List<DetailCourse>>() {
            @Override
            public void onResponse(Call<List<DetailCourse>> call, Response<List<DetailCourse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    filterAndDisplayCourses(response.body());
                } else {
                    Toast.makeText(getContext(), "Không thể tải khóa học", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DetailCourse>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTutorDetail(Tutor tutor) {
        textFullName.setText(tutor.getFullName() != null ? tutor.getFullName() : "N/A");
        textAverageRating.setText(tutor.getAverageRating() != null ? String.format("%.1f", tutor.getAverageRating()) : "N/A");
        textTotalSessions.setText(tutor.getTotalSessions() != null ? String.valueOf(tutor.getTotalSessions()) : "N/A");
        textExperienceYears.setText(tutor.getExperienceYears() != null ? tutor.getExperienceYears() + " năm" : "N/A");
        textDateOfBirth.setText(tutor.getDateOfBirth() != null ? tutor.getDateOfBirth() : "N/A");
        textPhone.setText(tutor.getPhone() != null ? tutor.getPhone() : "N/A");
        textAddress.setText(tutor.getAddress() != null ? tutor.getAddress() : "N/A");
        textBio.setText(tutor.getBio() != null ? tutor.getBio() : "Không có thông tin");

        String imageUrl = RetrofitClient.getFileUrl(tutor.getProfileImage());
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).placeholder(R.drawable.ic_account_box).error(R.drawable.ic_account_box).into(imgTutor);
        } else {
            imgTutor.setImageResource(R.drawable.ic_account_box);
        }
    }

    private void filterAndDisplayCourses(List<DetailCourse> courses) {
        newCourses.clear();

        for (DetailCourse course : courses) {
            if ("NEW".equals(course.getStatus())) {
                newCourses.add(course);
            }
        }

        displayCourses();
    }

    private void displayCourses() {
        layoutCourses.removeAllViews();

        if (newCourses.isEmpty()) {
            TextView emptyView = new TextView(getContext());
            emptyView.setText("Không có khóa học đang mở");
            emptyView.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));
            layoutCourses.addView(emptyView);
            return;
        }

        for (DetailCourse course : newCourses) {
            View courseView = getLayoutInflater().inflate(R.layout.item_course_fragment_tutor_detail_st, layoutCourses, false);
            bindCourseView(courseView, course);
            courseView.setOnClickListener(v -> navigateToCourseDetail(course.getId()));
            layoutCourses.addView(courseView);
        }
    }

    private void bindCourseView(View view, DetailCourse course) {
        TextView txtSubject = view.findViewById(R.id.txtCourseSubject);
        TextView txtPrice = view.findViewById(R.id.txtCoursePrice);
        TextView txtTime = view.findViewById(R.id.txtCourseTime);

        txtSubject.setText("Môn học: " + (course.getSubject() != null ? course.getSubject() : "N/A"));
        txtPrice.setText("Học phí: " + (course.getTotalPrice() > 0 ? formatCurrency(course.getTotalPrice()) : "N/A"));
        txtTime.setText("Thời gian: " + (course.getTimeOfTheLesson() != null ? course.getTimeOfTheLesson() : "N/A"));
    }

    private void navigateToCourseDetail(int courseId) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main_container, CourseDetailFragment_st.newInstance(courseId))
                .addToBackStack(null)
                .commit();
    }

    private String formatCurrency(double amount) {
        return NumberFormat.getInstance(new Locale("vi", "VN")).format(amount) + "đ";
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}