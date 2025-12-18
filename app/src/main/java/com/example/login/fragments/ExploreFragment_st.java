package com.example.login.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
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
                    Log.d(TAG, "Loaded " + allTutors.size() + " tutors");
                    displayTopTutors();
                } else {
                    Log.e(TAG, "Error loading tutors: " + response.code());
                    Toast.makeText(getContext(), "Lỗi tải danh sách gia sư: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tutor>> call, Throwable t) {
                Log.e(TAG, "Failed to load tutors", t);
                Toast.makeText(getContext(), "Lỗi tải danh sách gia sư: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Log.d(TAG, "Loaded " + allCourses.size() + " courses");
                    displayCourses();
                } else {
                    Log.e(TAG, "Error loading courses: " + response.code());
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "Error body: " + errorBody);
                    } catch (Exception e) {
                        Log.e(TAG, "Cannot read error body", e);
                    }
                    Toast.makeText(getContext(), "Lỗi tải danh sách khóa học: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CourseWithTutorDetail>> call, Throwable t) {
                Log.e(TAG, "Failed to load courses", t);
                Toast.makeText(getContext(), "Lỗi tải danh sách khóa học: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayTopTutors() {
        layoutTopTutors.removeAllViews();

        List<Tutor> topTutors = getTopTutors();

        for (Tutor tutor : topTutors) {
            View tutorView = createTutorView(tutor);
            layoutTopTutors.addView(tutorView);
        }
    }

    private List<Tutor> getTopTutors() {
        List<Tutor> sortedTutors = new ArrayList<>(allTutors);
        sortedTutors.sort((t1, t2) -> {
            double score1 = calculateTutorScore(t1);
            double score2 = calculateTutorScore(t2);
            return Double.compare(score2, score1);
        });

        return sortedTutors.size() > 3 ? sortedTutors.subList(0, 3) : sortedTutors;
    }

    private double calculateTutorScore(Tutor tutor) {
        int sessions = tutor.getTotalSessions() != null ? tutor.getTotalSessions() : 0;
        double rating = tutor.getAverageRating() != null ? tutor.getAverageRating() : 0;
        return sessions * rating;
    }

    private View createTutorView(Tutor tutor) {
        LinearLayout tutorLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        tutorLayout.setLayoutParams(params);
        tutorLayout.setOrientation(LinearLayout.VERTICAL);
        tutorLayout.setPadding(8, 8, 8, 8);

        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(90)
        );
        imageView.setLayoutParams(imageParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String imageUrl = RetrofitClient.getFileUrl(tutor.getProfileImage());
        Log.d(TAG, "Loading image from: " + imageUrl);

        if (imageUrl != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.tutor_image)
                    .error(R.drawable.tutor_image)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.tutor_image);
        }

        TextView nameView = new TextView(getContext());
        nameView.setText(tutor.getFullName() != null ? tutor.getFullName() : "N/A");

        TextView ratingView = new TextView(getContext());
        ratingView.setText("Đánh giá: " + (tutor.getAverageRating() != null ?
                String.format("%.1f", tutor.getAverageRating()) : "N/A"));

        TextView addressView = new TextView(getContext());
        addressView.setText(tutor.getAddress() != null ? tutor.getAddress() : "N/A");

        tutorLayout.addView(imageView);
        tutorLayout.addView(nameView);
        tutorLayout.addView(ratingView);
        tutorLayout.addView(addressView);

        return tutorLayout;
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
            View courseView = createCourseView(course);
            layoutCourses.addView(courseView);
        }
    }

    private View createCourseView(CourseWithTutorDetail course) {
        LinearLayout courseLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, dpToPx(12));
        courseLayout.setLayoutParams(params);
        courseLayout.setOrientation(LinearLayout.VERTICAL);
        courseLayout.setBackgroundColor(0xFFEEEEEE);
        courseLayout.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));

        TextView titleView = new TextView(getContext());
        titleView.setText(course.getSubject() != null ? course.getSubject() : "N/A");

        TextView priceView = new TextView(getContext());
        String formattedPrice = course.getTotalPrice() != null ?
                formatCurrency(course.getTotalPrice()) : "N/A";
        priceView.setText("Học phí: " + formattedPrice);

        TextView timeView = new TextView(getContext());
        timeView.setText("Thời gian: " + (course.getTimeOfTheLesson() != null ?
                course.getTimeOfTheLesson() : "N/A"));

        TextView tutorView = new TextView(getContext());
        tutorView.setText("Gia sư: " + (course.getFullName() != null ?
                course.getFullName() : "N/A"));

        courseLayout.addView(titleView);
        courseLayout.addView(priceView);
        courseLayout.addView(timeView);
        courseLayout.addView(tutorView);

        return courseLayout;
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
            boolean matchSearch = searchText.isEmpty() ||
                    (course.getSubject() != null && course.getSubject().toLowerCase().contains(searchText));

            boolean matchAddress = addressText.isEmpty() ||
                    (course.getAddress() != null && course.getAddress().toLowerCase().contains(addressText));

            boolean matchSubject = subjectText.isEmpty() ||
                    (course.getSubject() != null && course.getSubject().toLowerCase().contains(subjectText));

            boolean matchRating = true;
            if (course.getAverageRating() != null) {
                if (ratingFrom != null && course.getAverageRating() < ratingFrom) matchRating = false;
                if (ratingTo != null && course.getAverageRating() > ratingTo) matchRating = false;
            }

            boolean matchPrice = true;
            if (course.getTotalPrice() != null) {
                if (priceFrom != null && course.getTotalPrice() < priceFrom) matchPrice = false;
                if (priceTo != null && course.getTotalPrice() > priceTo) matchPrice = false;
            }

            if (matchSearch && matchAddress && matchSubject && matchRating && matchPrice) {
                filteredCourses.add(course);
            }
        }

        displayCourses();
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
        if (layoutFilter.getVisibility() == View.GONE) {
            layoutFilter.setVisibility(View.VISIBLE);
        } else {
            layoutFilter.setVisibility(View.GONE);
        }
    }

    private Double parseDouble(String text) {
        try {
            return text.isEmpty() ? null : Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount) + "đ";
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}