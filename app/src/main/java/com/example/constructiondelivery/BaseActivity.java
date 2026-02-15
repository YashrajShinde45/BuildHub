package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupUniversalNavigation();
    }

    protected void setupUniversalNavigation() {
        // Header Buttons
        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish()); // Default back action
        }

        View btnCart = findViewById(R.id.btnCart);
        if (btnCart != null) {
            btnCart.setOnClickListener(v -> {
                if (!(this instanceof CartActivity)) {
                    startActivity(new Intent(this, CartActivity.class));
                }
            });
        }

        // Footer Buttons
        View navHome = findViewById(R.id.navHome);
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                if (!(this instanceof UserDashboardActivity)) {
                    Intent intent = new Intent(this, UserDashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        }

        View navCompare = findViewById(R.id.navCompare);
        if (navCompare != null) {
            navCompare.setOnClickListener(v -> {
                if (!(this instanceof CompareActivity)) {
                    startActivity(new Intent(this, CompareActivity.class));
                }
            });
        }

        View navOrders = findViewById(R.id.navOrders);
        if (navOrders != null) {
            navOrders.setOnClickListener(v -> {
                if (!(this instanceof OrdersActivity)) {
                    startActivity(new Intent(this, OrdersActivity.class));
                }
            });
        }

        View navAccount = findViewById(R.id.navAccount);
        if (navAccount != null) {
            navAccount.setOnClickListener(v -> {
                if (!(this instanceof AccountActivity)) {
                    startActivity(new Intent(this, AccountActivity.class));
                }
            });
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
