package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.view.View;

import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
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
        radioRoleGroup = findViewById(R.id.radioRoleGroup);

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

        // ✅ Lấy vai trò người dùng
        int selectedId = radioRoleGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Vui lòng chọn vai trò (Học viên hoặc Gia sư)", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadio = findViewById(selectedId);
        String roleText = selectedRadio.getText().toString();
        String role;

        if (roleText.equalsIgnoreCase("Học viên")) {
            role = "STUDENT";
        } else {
            role = "TUTOR";
        }

        if (username.isEmpty() || email.isEmpty() || passwordHash.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        } else if (passwordHash.length() < 8) {
            Toast.makeText(this, "Mật khẩu phải ít nhất 8 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Gửi dữ liệu về server
        User user = new User(username, email, passwordHash, role);

        Call<User> call = apiService.registerUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Không kết nối được server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
