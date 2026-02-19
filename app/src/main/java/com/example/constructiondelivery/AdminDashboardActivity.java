package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    Button btnMaterials, btnOrders, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnMaterials = findViewById(R.id.btnMaterials);
        btnOrders = findViewById(R.id.btnOrders);
        btnLogout = findViewById(R.id.btnLogout);

        btnMaterials.setOnClickListener(v -> {
            Intent intent = new Intent(this, ManageMaterialActivity.class);
            startActivity(intent);
        });

        btnOrders.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewOrderActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
