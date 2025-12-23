package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.adapter.ManagermentCourseAdapter_admin;
import com.example.login.adapter.ListSessionOfCourseAdapter;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.CourseInfo;
import com.example.login.model.DetailCourse;
import com.example.login.model.SessionInfo;

import java.util.List;

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

import com.example.login.adapter.ListSessionOfCourseAdapter;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.DetailCourse;
import com.example.login.model.SessionInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagermentConfirmCourse_admin extends AppCompatActivity {

    ImageButton btnBack;
    ListView listViewCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_managerment_confirm_course);

        listViewCourse = findViewById(R.id.listViewCourse);
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        // ✅ GỌI LOAD DATA
        loadCourses("UNCONFIRM"); // hoặc CONFIRMED / REJECTED
    }

    private void loadCourses(String status) {

        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<List<CourseInfo>> call = apiService.getCourseByAdmin(status);

        call.enqueue(new Callback<List<CourseInfo>>() {
            @Override
            public void onResponse(Call<List<CourseInfo>> call, Response<List<CourseInfo>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<CourseInfo> courseList = response.body();

                    if (courseList.isEmpty()) {
                        Toast.makeText(
                                ManagermentConfirmCourse_admin.this,
                                "Không có khóa học",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    ManagermentCourseAdapter_admin adapter =
                            new ManagermentCourseAdapter_admin(
                                    ManagermentConfirmCourse_admin.this,
                                    courseList
                            );

                    listViewCourse.setAdapter(adapter);

                    listViewCourse.setOnItemClickListener((parent, view, position, id) -> {
                        CourseInfo course = courseList.get(position);

                        Intent intent = new Intent(
                                ManagermentConfirmCourse_admin.this,
                                DetailCourseActivity_admin.class
                        );
                        intent.putExtra("COURSE_ID_KEY", course.getId());
                        startActivity(intent);
                    });

                } else {
                    Toast.makeText(
                            ManagermentConfirmCourse_admin.this,
                            "Lỗi tải dữ liệu",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<List<CourseInfo>> call, Throwable t) {
                Toast.makeText(
                        ManagermentConfirmCourse_admin.this,
                        "Lỗi mạng: " + t.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}
