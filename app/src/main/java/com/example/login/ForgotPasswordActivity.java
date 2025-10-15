package com.example.login;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
public class ForgotPasswordActivity  extends AppCompatActivity {
    private TextView tvBackLogin;
    private EditText edtEmail;
    private Button btnSend;
//
//    android:id="@+id/edtEmail"
//    android:id="@+id/btnSend"
//    android:id="@+id/FtvBackLogin"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        tvBackLogin = findViewById(R.id.FtvBackLogin);
        btnSend = findViewById(R.id.btnSend);
        edtEmail = findViewById(R.id.edtEmail);



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Toast.makeText(ForgotPasswordActivity.this, "Xử lý mail", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        tvBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });





        return;
//
//        tvWelcome = findViewById(R.id.tvWelcome);
//
//        String username = getIntent().getStringExtra("username");
//        if (username == null || username.isEmpty()) {
//            username = "Khách";
//        }
//
//        tvWelcome.setText("Xin chào, " + username + "!");
//    }
    }
}
