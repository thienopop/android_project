package com.example.login;



import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.login.fragments.SessionsTodayFragment;
import com.example.login.fragments.MessagesFragment;
import com.example.login.fragments.ProfileFragment;
import com.example.login.fragments.SessionsFragment;
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
                loadFragment(new SessionsTodayFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_sessions) {
                loadFragment(new SessionsFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_messages) {
                loadFragment(new MessagesFragment());
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
