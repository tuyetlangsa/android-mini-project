package com.example.miniproject1_prm392;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText inputUsername, inputPassword;
    private Button btnLogin;
    private Button btnGoToSignUp; // new button for navigation
    private CheckBox rememberMe;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // layout bạn paste ở trên

        // Ánh xạ view
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToSignUp = findViewById(R.id.btnGoToSignUp); // map new button

        // Xử lý khi bấm login
        btnLogin.setOnClickListener(v -> {
            String username = inputUsername.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            } else if (username.equals("admin") && password.equals("123")) {
                Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();

                // Chuyển qua Bet88
                Intent intent = new Intent(LoginActivity.this,BetActivity.class);
                startActivity(intent);
                finish(); // đóng LoginActivity để tránh quay lại
            } else {
                Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý đi tới màn hình Sign Up
        if (btnGoToSignUp != null) {
            btnGoToSignUp.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            });
        }
    }
}
