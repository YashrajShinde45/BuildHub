package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView txtLoginTitle;

    private static final String USER_EMAIL = "user@gmail.com";
    private static final String USER_PASSWORD = "user123";

    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "admin123";

    boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtLoginTitle = findViewById(R.id.txtLoginTitle);

        String role = getIntent().getStringExtra("ROLE");
        if ("ADMIN".equals(role)) {
            isAdmin = true;
            txtLoginTitle.setText("Admin Login");
        } else {
            txtLoginTitle.setText("User Login");
        }

        btnLogin.setOnClickListener(v -> validateLogin());
    }

    private void validateLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // ✅ EMPTY CHECK
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isAdmin) {
            // ADMIN LOGIN
            if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {

                Toast.makeText(this, "Admin Login Success", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Invalid Admin Credentials", Toast.LENGTH_SHORT).show();
            }

        } else {
            // USER LOGIN
            if (email.equals(USER_EMAIL) && password.equals(USER_PASSWORD)) {

                Toast.makeText(this, "User Login Success", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, UserDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Invalid User Credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
