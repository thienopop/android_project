package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import com.example.login.api.ApiService;
import com.example.login.api.PrefsHelper;
import com.example.login.api.RetrofitClient;
import com.example.login.model.User;
import com.example.login.model.RegisterLoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvRegister;

    private ApiService apiService;  // ✅ Retrofit API interface

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ view
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.FtvForgotPassword);
        tvRegister = findViewById(R.id.FtvRegister);

        // ✅ Khởi tạo Retrofit
        apiService = RetrofitClient.getClient(this).create(ApiService.class);

        // --- Xử lý khi nhấn nút Đăng nhập ---
        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Tạo user gửi đi
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            // ✅ Gọi API đăng nhập
            Call<RegisterLoginResponse> call = apiService.login(user);
            call.enqueue(new Callback<RegisterLoginResponse>() {
                @Override
                public void onResponse(Call<RegisterLoginResponse> call, Response<RegisterLoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        RegisterLoginResponse loginResponse = response.body();

                        String message = loginResponse.getMessage();  // ✅ Lấy message từ JSON
                        String token = loginResponse.getToken();      // ✅ Lấy token từ JSON
                        int currentUserId = loginResponse.getCurrentUserId();    // ✅ Lấy currentUserId từ JSON

                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                        // 🔒 Lưu token vào SharedPreferences để dùng sau
                        PrefsHelper.saveToken(LoginActivity.this, token);
                        PrefsHelper.saveCurrentUserId(LoginActivity.this, currentUserId);
//cáh lấy token: String token = PrefsHelper.getToken(this);

//                        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
//                        prefs.edit().putString("token", token).apply();

                        // 👉 Chuyển sang màn hình chính
                        Intent intent = new Intent(LoginActivity.this, TutorDashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // ❌ Xử lý khi login thất bại (ví dụ sai tài khoản hoặc lỗi server)
                        try {
                            // Lấy thông báo lỗi trả về từ server (nếu có)
                            String errorBody = response.errorBody().string();
                            Toast.makeText(LoginActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterLoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // --- Quên mật khẩu ---
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // --- Đăng ký ---
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
