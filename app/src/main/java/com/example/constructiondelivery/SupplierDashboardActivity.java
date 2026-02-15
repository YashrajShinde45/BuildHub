package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SupplierDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_dashboard);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // --- BACK BUTTON LOGIC ---
        View backButton = findViewById(R.id.btnBack);
        if (backButton != null) {
            backButton.setVisibility(View.VISIBLE);
            backButton.setOnClickListener(v -> finish());
        }

        findViewById(R.id.btnAddProduct).setOnClickListener(v -> {
            startActivity(new Intent(this, AddProductActivity.class));
        });
    }
}
