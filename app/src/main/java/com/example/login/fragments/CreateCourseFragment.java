package com.example.login.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.login.DetailCourseActivity;
import com.example.login.R;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.CourseInfo;
import com.example.login.model.Course;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCourseFragment extends Fragment {

    private EditText edtSubject, edtNotes, edtTimeOfTheLesson, edtTotalPrice, edtTotalSessions;
    private Button btnAddCourse;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Khởi tạo ApiService
        apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);

        // 2. Khởi tạo các View
        edtSubject = view.findViewById(R.id.edtSubject);
        edtNotes = view.findViewById(R.id.edtNotes);
        edtTimeOfTheLesson = view.findViewById(R.id.edtTimeOfTheLesson);
        edtTotalPrice = view.findViewById(R.id.edtTotalPrice);
        edtTotalSessions = view.findViewById(R.id.edtTotalSessions);
        btnAddCourse = view.findViewById(R.id.btnAddCourse);

        // 3. Thiết lập Listener
        btnAddCourse.setOnClickListener(v -> {
            // ... (Phần lấy string và kiểm tra lỗi đã đúng) ...
            String subject = edtSubject.getText().toString().trim();
            String notes = edtNotes.getText().toString().trim();
            String timeOfTheLesson = edtTimeOfTheLesson.getText().toString().trim();
            String totalPriceString = edtTotalPrice.getText().toString().trim();
            String totalSessionsString = edtTotalSessions.getText().toString().trim();

            if (subject.isEmpty() || notes.isEmpty() || timeOfTheLesson.isEmpty() ||
                    totalPriceString.isEmpty() || totalSessionsString.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            int totalPrice;
            int totalSessions;
            try {
                totalPrice = Integer.parseInt(totalPriceString);
                totalSessions = Integer.parseInt(totalSessionsString);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Giá tiền và Tổng số buổi phải là số!", Toast.LENGTH_SHORT).show();
                return;
            }

            Course newCourse = new Course(subject, notes, timeOfTheLesson, totalPrice, totalSessions);

            // SỬA LỖI 1: Phải là Call<Void>
            // (Giả sử ApiService.AddCourse đã trả về Call<Void>)
            Call<Integer> call = apiService.AddCourse(newCourse);

            // Xử lý Callback<Void>
            call.enqueue(new Callback<Integer>() {
                @Override

                public void onResponse(Call<Integer> call, Response<Integer> response) {


                    if (response.isSuccessful()) {

                        Toast.makeText(getContext(), "Thêm khoá học thành công!", Toast.LENGTH_SHORT).show();

//                        chuyển đến thêm buổi học
                        Integer courseId = response.body();
                        Intent intent = new Intent(requireContext(), DetailCourseActivity.class);


                        intent.putExtra("COURSE_ID_KEY", courseId);
                        startActivity(intent);




                        if (getActivity() != null) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }


                    } else {
                        // Server trả về lỗi
                        Toast.makeText(getContext(), "Thêm thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override

                public void onFailure(Call<Integer> call, Throwable t) {
                    // Lỗi mạng hoặc kết nối

                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }



}