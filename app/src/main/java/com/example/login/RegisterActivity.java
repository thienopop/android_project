package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.view.View;

import com.example.login.api.ApiService;
import com.example.login.api.PrefsHelper;
import com.example.login.api.RetrofitClient;
import com.example.login.model.RegisterLoginResponse;
import com.example.login.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername, etEmail, etPassword;
    Button btnRegister;
    TextView tvRegister;
    ApiService apiService;
    RadioGroup radioRoleGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvRegister = findViewById(R.id.FtvBackLogin);
//        radioRoleGroup = findViewById(R.id.radioRoleGroup);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        btnRegister.setOnClickListener(v -> registerUser());
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String passwordHash = etPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || passwordHash.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        } else if (passwordHash.length() < 8) {
            Toast.makeText(this, "Mật khẩu phải ít nhất 8 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔸 Tạm thời cố định vai trò (có thể thay bằng radio button sau)
        String role = "TUTOR";

        // ✅ Tạo đối tượng user để gửi request
        User user = new User(username, email, passwordHash, role);

        // ✅ Gọi API đăng ký
        Call<RegisterLoginResponse> call = apiService.registerUser(user);
        call.enqueue(new Callback<RegisterLoginResponse>() {
            @Override
            public void onResponse(Call<RegisterLoginResponse> call, Response<RegisterLoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterLoginResponse loginResponse = response.body();
                    String message = loginResponse.getMessage();
                    String token = loginResponse.getToken();

                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

                    // 🔒 Lưu token
                    PrefsHelper.saveToken(RegisterActivity.this, token);

                    // 👉 Chuyển sang MainActivity
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // ⚠️ Khi response.body() là null, cần kiểm tra tránh lỗi NullPointerException
                    String message = "Đăng ký thất bại. Vui lòng kiểm tra lại thông tin.";
                    if (response.errorBody() != null) {
                        message = "Lỗi: " + response.message();
                    }
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterLoginResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
