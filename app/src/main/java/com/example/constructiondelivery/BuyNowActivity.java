package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BuyNowActivity extends BaseActivity {

    private Material material;
    private int quantity;
    private double totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 🔥 SAFELY GET MATERIAL
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            material = getIntent().getSerializableExtra("material", Material.class);
        } else {
            material = (Material) getIntent().getSerializableExtra("material");
        }

        quantity = getIntent().getIntExtra("quantity", 1);

        if (material == null) {
            Toast.makeText(this, "Error loading product", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 🔥 POPULATE UI
        ((ImageView) findViewById(R.id.imgBuyNowProduct))
                .setImageResource(material.image);

        ((TextView) findViewById(R.id.txtBuyNowName))
                .setText(material.name);

        ((TextView) findViewById(R.id.txtBuyNowQuantity))
                .setText("Quantity: " + quantity);

        calculateTotal();

        // 🔥 CONFIRM ORDER BUTTON → OPEN PAYMENT
        findViewById(R.id.btnConfirmOrder).setOnClickListener(v -> {

            Intent intent = new Intent(BuyNowActivity.this,
                    PaymentMethodActivity.class);

            // ✅ PASS PRODUCT NAME (NOT ID)
            intent.putExtra("materialName", material.name);

            intent.putExtra("quantity", quantity);
            intent.putExtra("totalAmount", totalAmount);

            startActivity(intent);
        });
    }

    // 🔥 CALCULATE TOTAL PROPERLY
    private void calculateTotal() {

        try {
            String cleanPrice = material.price
                    .replaceAll("[₹,]", "")
                    .split("/")[0]
                    .trim();

            double parsedPrice = Double.parseDouble(cleanPrice);

            totalAmount = parsedPrice * quantity;

        } catch (Exception e) {
            totalAmount = 0;
        }

        ((TextView) findViewById(R.id.txtTotalAmount))
                .setText("₹" + String.format("%.2f", totalAmount));
    }
}
