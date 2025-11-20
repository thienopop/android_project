package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

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
    RadioGroup radioGrRole;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Views
        radioGrRole = findViewById(R.id.radioGrRole);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvRegister = findViewById(R.id.FtvBackLogin);

        apiService = RetrofitClient.getClient(this).create(ApiService.class);

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
        String password = etPassword.getText().toString().trim();
        int selectedId = radioGrRole.getCheckedRadioButtonId();

        // 1. Validate Role Selection
        if (selectedId == -1) {
            Toast.makeText(this, "Vui lòng chọn vai trò!", Toast.LENGTH_SHORT).show();
            return; // STOP execution here
        }

        // 2. Get Role
        // Note: It is safer to check IDs (e.g., R.id.rbTutor) than text strings
        RadioButton selectedRadioButton = findViewById(selectedId);
        String selectedText = selectedRadioButton.getText().toString();

        if(selectedText.equals("Giáo viên")) {
            role = "TUTOR";
        } else {
            role = "STUDENT";
        }

        // 3. Validate Inputs
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8) {
            Toast.makeText(this, "Mật khẩu phải ít nhất 8 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Show Loading (Optional but recommended)
        btnRegister.setEnabled(false);
        btnRegister.setText("Đang xử lý...");

        // 5. API Call
        User user = new User(username, email, password, role);
        Call<RegisterLoginResponse> call = apiService.registerUser(user);

        call.enqueue(new Callback<RegisterLoginResponse>() {
            @Override
            public void onResponse(Call<RegisterLoginResponse> call, Response<RegisterLoginResponse> response) {
                // Re-enable button
                btnRegister.setEnabled(true);
                btnRegister.setText("Đăng Ký");

                if (response.isSuccessful() && response.body() != null) {
                    RegisterLoginResponse loginResponse = response.body();
                    String message = loginResponse.getMessage();

                    // Check if API returns a token on register.
                    // If not, you might want to redirect to LoginActivity instead.
                    String token = loginResponse.getToken();

                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                    if (token != null) {
                        PrefsHelper.saveToken(RegisterActivity.this, token);
                        navigateToDashboard();
                    } else {
                        // If API doesn't return token on register, go to login
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    String errorMsg = "Đăng ký thất bại.";
                    try {
                        if (response.errorBody() != null) {
                            // Try to read the error message from server
                            errorMsg = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterLoginResponse> call, Throwable t) {
                btnRegister.setEnabled(true);
                btnRegister.setText("Đăng Ký");
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("RegisterError", t.getMessage());
            }
        });
    }

    private void navigateToDashboard() {
        Intent intent;
        if ("STUDENT".equals(role)) {
            intent = new Intent(RegisterActivity.this, StudentDashboardActivity.class);
        } else {
            intent = new Intent(RegisterActivity.this, TutorDashboardActivity.class);
        }
        startActivity(intent);
        finishAffinity(); // Clear all previous activities so Back button exits app
    }
}