package com.example.constructiondelivery;

import android.os.Bundle;
import android.view.View;

public class OrdersActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    protected void setupUniversalNavigation() {
        super.setupUniversalNavigation();

        // Make the back button visible for this screen
        View backButton = findViewById(R.id.btnBack);
        if (backButton != null) {
            backButton.setVisibility(View.VISIBLE);
        }
    }
}
