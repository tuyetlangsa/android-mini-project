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
    private Button btnGoToSignUp;
    private CheckBox rememberMe;
    private TextView forgotPassword;
    private String registeredUsername = "admin";
    private String registeredPassword = "123";
    private String registeredEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToSignUp = findViewById(R.id.btnGoToSignUp);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("username")) {
            registeredUsername = intent.getStringExtra("username");
            registeredPassword = intent.getStringExtra("password");
            registeredEmail = intent.getStringExtra("email");

        }

        btnLogin.setOnClickListener(v -> {
            String usernameOrEmail = inputUsername.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (usernameOrEmail.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (registeredUsername == null || registeredPassword == null) {
                Toast.makeText(LoginActivity.this, "No account found. Please sign up.", Toast.LENGTH_LONG).show();
            } else {
                boolean isValidLogin = (usernameOrEmail.equals(registeredUsername) || usernameOrEmail.equals(registeredEmail))
                        && password.equals(registeredPassword);

                if (isValidLogin) {
                    Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                    Intent homeIntent = new Intent(LoginActivity.this, HomePageActivity.class);
                    startActivity(homeIntent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (btnGoToSignUp != null) {
            btnGoToSignUp.setOnClickListener(v -> {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            });
        }
    }
}
