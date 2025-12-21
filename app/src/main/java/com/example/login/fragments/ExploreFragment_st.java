package com.example.login.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.login.R;
import com.example.login.api.RetrofitClient;
import com.example.login.api.ApiService;
import com.example.login.model.CourseWithTutorDetail;
import com.example.login.model.Tutor;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreFragment_st extends Fragment {
    private static final String TAG = "ExploreFragment_st";
    private LinearLayout layoutFilter;
    private ImageView btnFilter;
    private EditText searchCourse;
    private EditText filterAddress;
    private EditText filterSubject;
    private EditText filterRatingFrom;
    private EditText filterRatingTo;
    private EditText filterPriceFrom;
    private EditText filterPriceTo;
    private Button btnClearFilter;
    private Button btnApplyFilter;
    private LinearLayout layoutTopTutors;
    private LinearLayout layoutCourses;
    private ApiService apiService;
    private List<Tutor> allTutors = new ArrayList<>();
    private List<CourseWithTutorDetail> allCourses = new ArrayList<>();
    private List<CourseWithTutorDetail> filteredCourses = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore_st, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        setupListeners();
        loadData();
    }

    private void initViews(View view) {
        layoutFilter = view.findViewById(R.id.layoutFilter);
        btnFilter = view.findViewById(R.id.btnFilter);
        searchCourse = view.findViewById(R.id.searchCourse);
        filterAddress = view.findViewById(R.id.filterAddress);
        filterSubject = view.findViewById(R.id.filterSubject);
        filterRatingFrom = view.findViewById(R.id.filterRatingFrom);
        filterRatingTo = view.findViewById(R.id.filterRatingTo);
        filterPriceFrom = view.findViewById(R.id.filterPriceFrom);
        filterPriceTo = view.findViewById(R.id.filterPriceTo);
        btnClearFilter = view.findViewById(R.id.btnClearFilter);
        btnApplyFilter = view.findViewById(R.id.btnApplyFilter);
        layoutTopTutors = view.findViewById(R.id.layoutTopTutors);
        layoutCourses = view.findViewById(R.id.layoutCourses);
    }

    private void setupListeners() {
        btnFilter.setOnClickListener(v -> toggleFilterVisibility());
        searchCourse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btnApplyFilter.setOnClickListener(v -> {
            applyFilters();
            toggleFilterVisibility();
        });
        btnClearFilter.setOnClickListener(v -> clearFilters());
    }

    private void loadData() {
        loadTopTutors();
        loadCourses();
    }

    private void loadTopTutors() {
        apiService.getVerifiedTutors().enqueue(new Callback<List<Tutor>>() {
            @Override
            public void onResponse(Call<List<Tutor>> call, Response<List<Tutor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allTutors = response.body();
                    displayTopTutors();
                }
            }

            @Override
            public void onFailure(Call<List<Tutor>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải gia sư", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCourses() {
        apiService.getAvailableCoursesForStudent().enqueue(new Callback<List<CourseWithTutorDetail>>() {
            @Override
            public void onResponse(Call<List<CourseWithTutorDetail>> call, Response<List<CourseWithTutorDetail>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allCourses = response.body();
                    filteredCourses = new ArrayList<>(allCourses);
                    displayCourses();
                }
            }

            @Override
            public void onFailure(Call<List<CourseWithTutorDetail>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải khóa học", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTopTutors() {
        layoutTopTutors.removeAllViews();
        List<Tutor> topTutors = getTopTutors();
        for (Tutor tutor : topTutors) {
            View tutorView = getLayoutInflater().inflate(R.layout.item_tutor_fragment_explore_st, layoutTopTutors, false);
            bindTutorView(tutorView, tutor);
            tutorView.setOnClickListener(v -> navigateToTutorDetail(tutor.getId()));
            layoutTopTutors.addView(tutorView);
        }
    }

    private void bindTutorView(View view, Tutor tutor) {
        ImageView imgTutor = view.findViewById(R.id.imgTutor);
        TextView txtName = view.findViewById(R.id.txtTutorName);
        TextView txtRating = view.findViewById(R.id.txtTutorRating);
        TextView txtAddress = view.findViewById(R.id.txtTutorAddress);

        txtName.setText(tutor.getFullName() != null ? tutor.getFullName() : "N/A");
        txtRating.setText("Đánh giá: " + (tutor.getAverageRating() != null ? String.format("%.1f", tutor.getAverageRating()) : "N/A"));
        txtAddress.setText(tutor.getAddress() != null ? tutor.getAddress() : "N/A");

        String imageUrl = RetrofitClient.getFileUrl(tutor.getProfileImage());
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).placeholder(R.drawable.ic_account_box).error(R.drawable.ic_account_box).into(imgTutor);
        } else {
            imgTutor.setImageResource(R.drawable.ic_account_box);
        }
    }

    private void displayCourses() {
        layoutCourses.removeAllViews();
        if (filteredCourses.isEmpty()) {
            TextView emptyView = new TextView(getContext());
            emptyView.setText("Không có khóa học nào");
            emptyView.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));
            layoutCourses.addView(emptyView);
            return;
        }
        for (CourseWithTutorDetail course : filteredCourses) {
            View courseView = getLayoutInflater().inflate(R.layout.item_course_fragment_explore_st, layoutCourses, false);
            bindCourseView(courseView, course);
            courseView.setOnClickListener(v -> navigateToCourseDetail(course.getId()));
            layoutCourses.addView(courseView);
        }
    }

    private void bindCourseView(View view, CourseWithTutorDetail course) {
        TextView txtSubject = view.findViewById(R.id.txtCourseSubject);
        TextView txtPrice = view.findViewById(R.id.txtCoursePrice);
        TextView txtTime = view.findViewById(R.id.txtCourseTime);
        TextView txtTutor = view.findViewById(R.id.txtCourseTutor);

        txtSubject.setText(course.getSubject() != null ? course.getSubject() : "N/A");
        txtPrice.setText("Học phí: " + (course.getTotalPrice() != null ? formatCurrency(course.getTotalPrice()) : "N/A"));
        txtTime.setText("Thời gian: " + (course.getTimeOfTheLesson() != null ? course.getTimeOfTheLesson() : "N/A"));
        txtTutor.setText("Gia sư: " + (course.getFullName() != null ? course.getFullName() : "N/A"));
    }

    private void navigateToTutorDetail(int tutorId) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main_container, TutorDetailFragment_st.newInstance(tutorId))
                .addToBackStack(null)
                .commit();
    }

    private void navigateToCourseDetail(int courseId) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main_container, CourseDetailFragment_st.newInstance(courseId))
                .addToBackStack(null)
                .commit();
    }

    private List<Tutor> getTopTutors() {
        List<Tutor> sortedTutors = new ArrayList<>(allTutors);
        sortedTutors.sort((t1, t2) -> Double.compare(calculateTutorScore(t2), calculateTutorScore(t1)));
        return sortedTutors.size() > 3 ? sortedTutors.subList(0, 3) : sortedTutors;
    }

    private double calculateTutorScore(Tutor tutor) {
        int sessions = tutor.getTotalSessions() != null ? tutor.getTotalSessions() : 0;
        double rating = tutor.getAverageRating() != null ? tutor.getAverageRating() : 0;
        return sessions * rating;
    }

    private void applyFilters() {
        filteredCourses.clear();
        String searchText = searchCourse.getText().toString().toLowerCase().trim();
        String addressText = filterAddress.getText().toString().toLowerCase().trim();
        String subjectText = filterSubject.getText().toString().toLowerCase().trim();
        Double ratingFrom = parseDouble(filterRatingFrom.getText().toString());
        Double ratingTo = parseDouble(filterRatingTo.getText().toString());
        Double priceFrom = parseDouble(filterPriceFrom.getText().toString());
        Double priceTo = parseDouble(filterPriceTo.getText().toString());

        for (CourseWithTutorDetail course : allCourses) {
            if (matchesFilters(course, searchText, addressText, subjectText, ratingFrom, ratingTo, priceFrom, priceTo)) {
                filteredCourses.add(course);
            }
        }
        displayCourses();
    }

    private boolean matchesFilters(CourseWithTutorDetail course, String searchText, String addressText, String subjectText, Double ratingFrom, Double ratingTo, Double priceFrom, Double priceTo) {
        if (!searchText.isEmpty() && (course.getSubject() == null || !course.getSubject().toLowerCase().contains(searchText)))
            return false;
        if (!addressText.isEmpty() && (course.getAddress() == null || !course.getAddress().toLowerCase().contains(addressText)))
            return false;
        if (!subjectText.isEmpty() && (course.getSubject() == null || !course.getSubject().toLowerCase().contains(subjectText)))
            return false;
        if (course.getAverageRating() != null) {
            if (ratingFrom != null && course.getAverageRating() < ratingFrom) return false;
            if (ratingTo != null && course.getAverageRating() > ratingTo) return false;
        }
        if (course.getTotalPrice() != null) {
            if (priceFrom != null && course.getTotalPrice() < priceFrom) return false;
            if (priceTo != null && course.getTotalPrice() > priceTo) return false;
        }
        return true;
    }

    private void clearFilters() {
        searchCourse.setText("");
        filterAddress.setText("");
        filterSubject.setText("");
        filterRatingFrom.setText("");
        filterRatingTo.setText("");
        filterPriceFrom.setText("");
        filterPriceTo.setText("");
        filteredCourses = new ArrayList<>(allCourses);
        displayCourses();
    }

    private void toggleFilterVisibility() {
        layoutFilter.setVisibility(layoutFilter.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    private Double parseDouble(String text) {
        try {
            return text.isEmpty() ? null : Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String formatCurrency(double amount) {
        return NumberFormat.getInstance(new Locale("vi", "VN")).format(amount) + "đ";
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}