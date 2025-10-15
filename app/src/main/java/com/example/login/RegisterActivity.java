package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class RegisterActivity extends AppCompatActivity {

//    android:id="@+id/edtFullName"
//
//    android:id="@+id/edtEmail"
//
//    android:id="@+id/edtPassword"
//
//    android:id="@+id/edtConfirmPassword"
//
//    android:id="@+id/btnRegister"
//    android:id="@+id/FtvBackLogin"



        private TextView tvBackLogin;
        private EditText edtFullName,edtEmail,edtPassword,edtConfirmPassword;
        private Button btnRegister;
//
//    android:id="@+id/edtEmail"
//    android:id="@+id/btnSend"
//    android:id="@+id/FtvBackLogin"

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
            tvBackLogin = findViewById(R.id.FtvBackLogin);
            edtFullName = findViewById(R.id.edtFullName);
            edtPassword = findViewById(R.id.edtPassword);
            edtEmail = findViewById(R.id.edtEmail);
            edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
            btnRegister = findViewById(R.id.btnRegister);



            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = edtEmail.getText().toString().trim();
                    String FullName = edtFullName.getText().toString().trim();
                    String Password = edtPassword.getText().toString().trim();
                    String ConfirmPassword = edtConfirmPassword.getText().toString().trim();

                    if (email.isEmpty()||FullName.isEmpty()||Password.isEmpty()||ConfirmPassword.isEmpty()) {
                        Toast.makeText(RegisterActivity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if (!Password.equals(ConfirmPassword)) {
                        Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                    } else if (ConfirmPassword.length() < 8) {
                        Toast.makeText(RegisterActivity.this, "Mật khẩu phải ít nhất 8 ký tự!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Xử lý đăng ký", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            tvBackLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });


}
}
