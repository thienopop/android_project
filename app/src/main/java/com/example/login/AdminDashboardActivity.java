package com.example.login;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.login.fragments.ChatsFragment_st;
import com.example.login.fragments.VerifiedTutorFragment;
import com.example.login.fragments.SettingFragment_admin;
import com.example.login.fragments.SessionFragment_st;
import com.example.login.fragments.AdminDashboardFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
     // Gắn sự kiện click vào các mục trong Bottom Navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_setting) {
                loadFragment(new SettingFragment_admin());
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