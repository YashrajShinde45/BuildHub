package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SupplierLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_login);

        EditText etEmail = findViewById(R.id.etSupplierEmail);
        EditText etPassword = findViewById(R.id.etSupplierPassword);
        Button btnLogin = findViewById(R.id.btnLoginSupplier);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.equals("sup@gmail.com") && password.equals("sup123")) {
                Toast.makeText(this, "Supplier Login Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, SupplierDashboardActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.tvGoToSupplierSignup).setOnClickListener(v -> {
            startActivity(new Intent(this, SupplierSignupActivity.class));
        });
    }
}
