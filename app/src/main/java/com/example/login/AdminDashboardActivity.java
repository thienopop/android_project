package com.example.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.fragments.ChatboxFragment;
import com.example.login.fragments.ChatsFragment;
import com.example.login.fragments.CreateCourseFragment;
import com.example.login.fragments.HomeFragment;
import com.example.login.fragments.ProfileFragment;
import com.example.login.model.ChatWithUserDetail;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.login.adapter.FagmentsSessionTodayAdapter;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.fragments.HomeFragment;
import com.example.login.fragments.ChatboxFragment;
import com.example.login.fragments.ChatsFragment;
import com.example.login.fragments.ProfileFragment;
import com.example.login.fragments.CreateCourseFragment;

import com.example.login.model.ChatWithUserDetail;
import com.example.login.model.CourseInfo;
import com.example.login.model.SessionInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminDashboardActivity extends AppCompatActivity {

    private TextView tutorManagement, studentManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);
        // Ánh xạ view
        tutorManagement = findViewById(R.id.tutor_management);
        studentManagement = findViewById(R.id.student_management);

    }
}
