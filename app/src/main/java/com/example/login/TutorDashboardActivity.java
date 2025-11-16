package com.example.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.login.fragments.HomeFragment;
import com.example.login.fragments.ChatsFragment;
import com.example.login.fragments.ProfileFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TutorDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_dashboard);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Gắn sự kiện click vào các mục trong Bottom Navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_sessions) {
                loadFragment(new HomeFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_chats) {
                loadFragment(new ChatsFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                loadFragment(new ProfileFragment());
                return true;
            }
            return false;
        });

        // Load Fragment mặc định khi mở Activity
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    // Hàm load Fragment vào FrameLayout
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }
}
