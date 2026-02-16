package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SupplierLoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvGoToSignup;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_login);

        // Initialize Views
        etEmail = findViewById(R.id.etSupplierEmail);
        etPassword = findViewById(R.id.etSupplierPassword);
        btnLogin = findViewById(R.id.btnLoginSupplier);
        tvGoToSignup = findViewById(R.id.tvGoToSupplierSignup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Login Button Click
        btnLogin.setOnClickListener(v -> loginSupplier());

        // Go To Signup Click
        tvGoToSignup.setOnClickListener(v -> {
            Intent intent = new Intent(SupplierLoginActivity.this, SupplierSignupActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loginSupplier() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter Email");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter Password");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        // Firebase Authentication Login
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Load the user's cart from Firestore
                        CartManager.loadCartFromFirestore(() -> {
                            Toast.makeText(SupplierLoginActivity.this,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT).show();

                            // Go to Supplier Dashboard
                            Intent intent = new Intent(SupplierLoginActivity.this, SupplierDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        String errorMessage = task.getException() != null ?
                                task.getException().getMessage() : "Authentication failed.";
                        Toast.makeText(SupplierLoginActivity.this,
                                "Login Failed: " + errorMessage,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}