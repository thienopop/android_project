package com.example.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.login.fragments.ExploreFragment_st;
import com.example.login.fragments.SessionFragment_st;
import com.example.login.fragments.ChatsFragment_st;
import com.example.login.fragments.ProfileFragment_st;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard_st);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_sessions) {
                loadFragment(new SessionFragment_st());
                return true;
            } else if (item.getItemId() == R.id.nav_explore){
                loadFragment(new ExploreFragment_st());
                return true;
            } else if (item.getItemId() == R.id.nav_chats) {
                loadFragment(new ChatsFragment_st());
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                loadFragment(new ProfileFragment_st());
                return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_sessions);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
    }
}