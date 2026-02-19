package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ProductDetailActivity extends BaseActivity {

    private int count = 1;
    private TextView txtCount;
    private Material material;
    private long possibleDeliveryTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_product_detail);

        // Make the back button visible for this screen
        View back = findViewById(R.id.btnBack);
        if (back != null) {
            back.setVisibility(View.VISIBLE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            material = getIntent().getSerializableExtra("material", Material.class);
        } else {
            material = (Material) getIntent().getSerializableExtra("material");
        }

        if (material == null) {
            Toast.makeText(this, "Product data not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- TIMER LOGIC ---
        TextView tvPossibleDelivery = findViewById(R.id.tvPossibleDelivery);
        possibleDeliveryTime = createRandomTimer();
        String timeStr = getRemainingTimeStr(possibleDeliveryTime);
        tvPossibleDelivery.setText("Possible delivery in: " + timeStr);

        // --- BUTTON LOGIC ---
        findViewById(R.id.btnAddToCart).setOnClickListener(v -> {
            CartItem item = new CartItem(material, count);
            CartManager.addItem(item);
            Toast.makeText(this, material.name + " added to cart", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnBuyNow).setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderSummaryActivity.class);
            intent.putExtra("material", material);
            intent.putExtra("quantity", count);
            startActivity(intent);
        });


        ImageView img = findViewById(R.id.imgProduct);
        TextView name = findViewById(R.id.txtName);
        TextView supplier = findViewById(R.id.txtSupplier);
        TextView price = findViewById(R.id.txtPrice);
        TextView desc = findViewById(R.id.txtDescription);
        TextView quality = findViewById(R.id.txtQuality);
        TextView details = findViewById(R.id.txtDetails);

        // QUANTITY SELECTOR LOGIC
        txtCount = findViewById(R.id.txtCount);
        TextView txtUnitDisplay = findViewById(R.id.txtUnitDisplay);
        Button btnIncrease = findViewById(R.id.btnIncrease);
        Button btnDecrease = findViewById(R.id.btnDecrease);

        // Use the new quantityUnit field
        if (material.quantityUnit != null && !material.quantityUnit.isEmpty()) {
            txtUnitDisplay.setText(" " + material.quantityUnit);
        } else {
            txtUnitDisplay.setText(" Units"); // Fallback
        }

        btnIncrease.setOnClickListener(v -> {
            count++;
            txtCount.setText(String.valueOf(count));
        });

        btnDecrease.setOnClickListener(v -> {
            if (count > 1) {
                count--;
                txtCount.setText(String.valueOf(count));
            }
        });

        img.setImageResource(material.image);
        name.setText(material.name);
        supplier.setText("Supplier: " + material.supplier);
        price.setText(material.price);
        desc.setText(material.shortDesc);
        quality.setText(material.quality);
        details.setText(material.details);
    }

    private long createRandomTimer() {
        long currentTime = System.currentTimeMillis();
        Random random = new Random();
        int randomDays = random.nextInt(4) + 4;  // 4 to 7 days
        int randomHours = random.nextInt(24);
        long randomDaysInMillis = randomDays * 24L * 60 * 60 * 1000;
        long randomHoursInMillis = randomHours * 60L * 60 * 1000;
        return currentTime + randomDaysInMillis + randomHoursInMillis;
    }

    private String getRemainingTimeStr(long possibleDeliveryTime) {
        long diff = possibleDeliveryTime - System.currentTimeMillis();
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff) % 24;
        return days + "d " + hours + "h";
    }
}
