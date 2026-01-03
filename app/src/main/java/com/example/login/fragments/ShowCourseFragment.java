
package com.example.login.fragments;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.example.login.api.ApiService;
import com.example.login.api.PrefsHelper;
import com.example.login.api.RetrofitClient;
import com.example.login.DetailCourseActivity;
import com.example.login.TutorDashboardActivity;
import com.example.login.model.User;
import com.example.login.model.RegisterLoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
// import android.graphics.Color; // <-- ĐÃ XÓA
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.login.R;
import com.example.login.adapter.FragmentsCourseAdapter;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.CourseInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowCourseFragment extends Fragment {

    private ListView listViewCourse;
    private TextView txtMessage;
    private String status = "ONGOING"; // Giá trị mặc định

    private Button btnShowPENDING, btnShowSTUDENT_REGISTERED,
            btnShowONGOING, btnShowCOMPLETED, btnShowCANCELLED;

    // Mảng này dùng để quản lý trạng thái selected
    private Button[] allButtons;

    public ShowCourseFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sup_course, container, false);

        listViewCourse = view.findViewById(R.id.listViewCourse);
        txtMessage = view.findViewById(R.id.txtMessage);

        // Ánh xạ các Button
        btnShowPENDING = view.findViewById(R.id.btnShowPENDING);
        btnShowSTUDENT_REGISTERED = view.findViewById(R.id.btnShowSTUDENT_REGISTERED);
        btnShowONGOING = view.findViewById(R.id.btnShowONGOING);
        btnShowCOMPLETED = view.findViewById(R.id.btnShowCOMPLETED);
        btnShowCANCELLED = view.findViewById(R.id.btnShowCANCELLED);

        // Gộp các button vào mảng
        allButtons = new Button[]{btnShowPENDING, btnShowSTUDENT_REGISTERED, btnShowONGOING, btnShowCOMPLETED, btnShowCANCELLED};

        // Gán sự kiện click
        setupButton(btnShowPENDING, "NEW");
        setupButton(btnShowSTUDENT_REGISTERED, "STUDENT_REGISTERED");
        setupButton(btnShowONGOING, "ONGOING");
        setupButton(btnShowCOMPLETED, "COMPLETED");
        setupButton(btnShowCANCELLED, "CANCELLED");

//        "NEW", "STUDENT_REGISTERED", "ONGOING", "COMPLETED", "CANCELLED");

        // Phục hồi trạng thái
        if (savedInstanceState != null) {
            status = savedInstanceState.getString("status", "ONGOING");
        }

        // Đặt trạng thái "selected" cho button ban đầu
        highlightSelectedButton(getButtonByStatus(status));
        loadCourses(status);

        return view;
    }

    private void loadCourses(String status) {
        if (getContext() == null) return;
        txtMessage.setText("Đang tải khóa học...");
        txtMessage.setVisibility(View.VISIBLE);
        listViewCourse.setAdapter(null);

        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        Call<List<CourseInfo>> call = apiService.getCourseByTutor(status);

        call.enqueue(new Callback<List<CourseInfo>>() {
            @Override
            public void onResponse(Call<List<CourseInfo>> call, Response<List<CourseInfo>> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    List<CourseInfo> courseList = response.body();

                    if (courseList.isEmpty()) {
                        txtMessage.setText("Không có khóa học với trạng thái: " + status);
                        txtMessage.setVisibility(View.VISIBLE);
                        return;
                    }

                    txtMessage.setVisibility(View.GONE);
                    FragmentsCourseAdapter adapter = new FragmentsCourseAdapter(getContext(), courseList);
                    listViewCourse.setAdapter(adapter);

                    listViewCourse.setOnItemClickListener((parent, view, position, id) -> {
                        CourseInfo course = courseList.get(position);

                        Intent intent = new Intent(requireContext(), DetailCourseActivity.class);
                    int courseId = course.getId();

                    intent.putExtra("COURSE_ID_KEY", courseId);
                    startActivity(intent);

                    });


//                    Intent intent = new Intent(this, DetailCourseActivity.class);
//
//
//                    int courseId = course.getId();
//
//
//                    intent.putExtra("COURSE_ID_KEY", courseId);
//// 4. Khởi động Activity mới
//                    startActivity(intent);

                } else {
                    txtMessage.setText("API trả về dữ liệu không hợp lệ!");
                    txtMessage.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<List<CourseInfo>> call, Throwable t) {
                if (!isAdded()) return;
                txtMessage.setText("Lỗi kết nối API: " + t.getMessage());
                txtMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupButton(Button button, String newStatus) {
        button.setOnClickListener(v -> {
            status = newStatus;
            highlightSelectedButton(button); // Chỉ cần gọi hàm này
            loadCourses(status);
        });
    }

    /**
     * HÀM ĐÃ ĐƯỢC TÁI CẤU TRÚC (Refactored)
     * Hàm này chỉ quản lý trạng thái (selected), không quản lý màu sắc.
     * Màu sắc sẽ tự động thay đổi dựa trên file XML Selector.
     */
    private void highlightSelectedButton(Button selectedButton) {
        // 1. Bỏ chọn TẤT CẢ các button
        for (Button btn : allButtons) {
            btn.setSelected(false);
        }

        // 2. Chỉ CHỌN button được click
        if (selectedButton != null) {
            selectedButton.setSelected(true);
        }
    }

    /**
     * HÀM NÀY KHÔNG CÒN CẦN THIẾT NỮA
     * private void resetButtonColors() { ... }
     */

    private Button getButtonByStatus(String status) {
        switch (status) {
            case "PENDING": return btnShowPENDING;
            case "STUDENT_REGISTERED": return btnShowSTUDENT_REGISTERED;
            case "ONGOING": return btnShowONGOING;
            case "COMPLETED": return btnShowCOMPLETED;
            case "CANCELLED": return btnShowCANCELLED;
            default: return btnShowONGOING;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("status", status);
        super.onSaveInstanceState(outState);
    }
}