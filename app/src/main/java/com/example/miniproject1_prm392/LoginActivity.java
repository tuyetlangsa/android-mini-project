package com.example.miniproject1_prm392;

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
    private CheckBox rememberMe;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ view từ XML
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        rememberMe = findViewById(R.id.rememberMe);
        forgotPassword = findViewById(R.id.forgotPassword);

        // Xử lý nút Login
        btnLogin.setOnClickListener(v -> {
            String username = inputUsername.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            } else if (username.equals("admin") && password.equals("123")) {
                Toast.makeText(this, "Login Success!", Toast.LENGTH_SHORT).show();
                // TODO: chuyển sang HomeActivity hoặc màn hình khác
                // startActivity(new Intent(this, HomeActivity.class));
                // finish();
            } else {
                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý forgot password
        forgotPassword.setOnClickListener(v ->
                Toast.makeText(this, "Forgot password clicked!", Toast.LENGTH_SHORT).show()
        );
    }
}
