package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SupplierSignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_signup);

        EditText etName = findViewById(R.id.etSupplierName);
        EditText etEmail = findViewById(R.id.etSupplierEmailSignup);
        EditText etPassword = findViewById(R.id.etSupplierPasswordSignup);
        Button btnSignup = findViewById(R.id.btnSignupSupplier);

        btnSignup.setOnClickListener(v -> {
            // For now, just show a success message and go to the supplier dashboard
            Toast.makeText(this, "Signup Successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SupplierDashboardActivity.class));
            finish();
        });

        findViewById(R.id.tvGoToSupplierLogin).setOnClickListener(v -> {
            // Finish current activity to go back to login screen
            finish();
        });
    }
}
