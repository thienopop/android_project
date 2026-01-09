package com.example.login;

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
            } else if (item.getItemId() == R.id.nav_create_course) {
                loadFragment(new CreateCourseFragment());
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
    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }

//
//    public void load(ChatWithUserDetail item) {
//        if (getActivity() != null) {
//            androidx.fragment.app.Fragment chat = com.example.login.fragments.ChatboxFragment.newInstance(
//                    item.getChatId(), item.getUserId(), item.getUsername(), item.getFullName());
//            getActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.main_container, chat)
//                    .addToBackStack(null)
//                    .commit();
//        }
//    }

    private void loadFragmentChat(ChatWithUserDetail item) {

        // 1. Khởi tạo fragment
        ChatboxFragment fragment = new ChatboxFragment();

        // 2. Tạo bundle
        Bundle bundle = new Bundle();
        bundle.putInt("CHAT_ID", item.getChatId());
        bundle.putInt("USER_ID", item.getUserId());
        bundle.putString("USERNAME", item.getUsername());
        bundle.putString("FULL_NAME", item.getFullName());

        // 3. Gán bundle cho fragment
        fragment.setArguments(bundle);

        // 4. Load fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack("ChatboxFragment") // cho phép back
                .commit();
    }

    private void findChatWithChatId(int chatId) {


        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<ChatWithUserDetail> call = apiService.getMyChatsId(chatId);

        call.enqueue(new Callback<ChatWithUserDetail>() {
            @Override
            public void onResponse(@NonNull Call<ChatWithUserDetail> call, @NonNull Response<ChatWithUserDetail> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ChatWithUserDetail chatWithUserDetail = response.body();

                    // TODO: Xử lý chatWithUserDetail, ví dụ load fragment chat
                    loadFragmentChat(chatWithUserDetail);

                } else {
                    Toast.makeText(TutorDashboardActivity.this, "Không tìm thấy chat.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChatWithUserDetail> call, @NonNull Throwable t) {

                Toast.makeText(TutorDashboardActivity.this, "Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}
