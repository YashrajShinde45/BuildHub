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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SupplierSignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etPhone;
    private Button btnSignup;
    private TextView tvGoToLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_signup);

        // Initialize Views
        etName = findViewById(R.id.etSupplierName);
        etEmail = findViewById(R.id.etSupplierEmailSignup);
        etPassword = findViewById(R.id.etSupplierPasswordSignup);
        etPhone = findViewById(R.id.etSupplierPhone);
        btnSignup = findViewById(R.id.btnSignupSupplier);
        tvGoToLogin = findViewById(R.id.tvGoToSupplierLogin);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Signup Button Click
        btnSignup.setOnClickListener(v -> registerSupplier());

        // Go To Login Click
        tvGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SupplierSignupActivity.this, SupplierLoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerSupplier() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Enter Name");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter Email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter Password");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Enter Phone Number");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        // Create user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        if (mAuth.getCurrentUser() != null) {
                            String supplierId = mAuth.getCurrentUser().getUid();

                            // Create supplier map
                            Map<String, Object> supplier = new HashMap<>();
                            supplier.put("supplierId", supplierId);
                            supplier.put("name", name);
                            supplier.put("email", email);
                            supplier.put("phone", phone);
                            // Removed password storage for security

                            // Save to Firestore
                            db.collection("suppliers")
                                    .document(supplierId)
                                    .set(supplier)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(SupplierSignupActivity.this,
                                                "Signup Successful",
                                                Toast.LENGTH_SHORT).show();

                                        // Go to Login Screen
                                        Intent intent = new Intent(SupplierSignupActivity.this, SupplierLoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(SupplierSignupActivity.this,
                                                "Firestore Error: " + e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    });
                        }

                    } else {
                        String errorMessage = task.getException() != null ?
                                task.getException().getMessage() : "Unknown error";
                        Toast.makeText(SupplierSignupActivity.this,
                                "Auth Error: " + errorMessage,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}