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
import com.example.login.fragments.ChatsFragment_st;
import com.example.login.fragments.VerifiedTutorFragment;
import com.example.login.fragments.CreateCourseFragment;
import com.example.login.fragments.HomeFragment;
import com.example.login.fragments.ProfileFragment;
import com.example.login.fragments.ProfileFragment_st;
import com.example.login.fragments.SessionFragment_st;
import com.example.login.fragments.AdminDashboardFragment;
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



import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.login.fragments.SessionFragment_st;
import com.example.login.fragments.ChatsFragment_st;
import com.example.login.fragments.ProfileFragment_st;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottom_navigation
//        android:id="@+id/nav_setting"
//    android:id="@+id/nav_complaints"  android:id="@+id/nav_manage"
//    android:id="@+id/nav_verify"  android:id="@+id/nav_dash"
//


        // Gắn sự kiện click vào các mục trong Bottom Navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_setting) {
                loadFragment(new SessionFragment_st());
                return true;
            } else if (item.getItemId() == R.id.nav_complaints) {
                loadFragment(new SessionFragment_st());
                return true;
            } else if (item.getItemId() == R.id.nav_manage) {
                loadFragment(new ChatsFragment_st());
                return true;
            } else if (item.getItemId() == R.id.nav_verify) {
                loadFragment(new VerifiedTutorFragment());
                return true;
            }
         else if (item.getItemId() == R.id.nav_dash) {
            loadFragment(new AdminDashboardFragment());
            return true;
        }
            return false;
        });

        // Load Fragment mặc định khi mở Activity
        bottomNavigationView.setSelectedItemId(R.id.nav_dash);
    }

    // Hàm load Fragment vào FrameLayout
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }
}