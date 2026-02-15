package com.example.constructiondelivery;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BuyNowActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // --- DEFINITIVE AND SAFE DATA RETRIEVAL ---
        Material material;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            material = getIntent().getSerializableExtra("material", Material.class);
        } else {
            material = (Material) getIntent().getSerializableExtra("material");
        }

        int quantity = getIntent().getIntExtra("quantity", 1);

        // --- CRASH PREVENTION ---
        if (material == null) {
            Toast.makeText(this, "Could not load product. Please try again.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity to prevent a crash
            return;
        }

        // --- POPULATE VIEWS ---
        // This code will only run if the material object is valid.
        ((ImageView) findViewById(R.id.imgBuyNowProduct)).setImageResource(material.image);
        ((TextView) findViewById(R.id.txtBuyNowName)).setText(material.name);
        ((TextView) findViewById(R.id.txtBuyNowQuantity)).setText("Quantity: " + quantity);
        ((TextView) findViewById(R.id.txtDeliveryAddress)).setText(DataHelper.getUserAddress()); // CRITICAL TYPO IS NOW FIXED

        calculateTotals(material, quantity);

        findViewById(R.id.btnConfirmOrder).setOnClickListener(v -> {
            Toast.makeText(this, "Order Confirmed!", Toast.LENGTH_SHORT).show();
        });
    }

    private void calculateTotals(Material material, int quantity) {
        double totalMrp = 0;
        String priceString = material.price.replaceAll("[₹,]", "").split("/")[0].trim();
        try {
            totalMrp = Double.parseDouble(priceString) * quantity;
        } catch (NumberFormatException e) {
            // Safely ignore if price is not a valid number
        }
        double discount = totalMrp * 0.1; // 10% discount
        double totalAmount = totalMrp - discount;

        ((TextView) findViewById(R.id.txtTotalMrp)).setText(String.format("₹%.2f", totalMrp));
        ((TextView) findViewById(R.id.txtDiscount)).setText(String.format("-₹%.2f", discount));
        ((TextView) findViewById(R.id.txtTotalAmount)).setText(String.format("₹%.2f", totalAmount));
    }

    @Override
    protected void setupUniversalNavigation() {
        super.setupUniversalNavigation();
        View backButton = findViewById(R.id.btnBack);
        if (backButton != null) {
            backButton.setVisibility(View.VISIBLE);
        }
    }
}
