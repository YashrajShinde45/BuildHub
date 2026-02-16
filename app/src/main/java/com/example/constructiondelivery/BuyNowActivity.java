package com.example.constructiondelivery;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BuyNowActivity extends BaseActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private Material material;
    private int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // --- SAFE DATA RETRIEVAL ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            material = getIntent().getSerializableExtra("material", Material.class);
        } else {
            material = (Material) getIntent().getSerializableExtra("material");
        }

        quantity = getIntent().getIntExtra("quantity", 1);

        if (material == null) {
            Toast.makeText(this, "Could not load product.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- POPULATE UI ---
        ((ImageView) findViewById(R.id.imgBuyNowProduct)).setImageResource(material.image);
        ((TextView) findViewById(R.id.txtBuyNowName)).setText(material.name);
        ((TextView) findViewById(R.id.txtBuyNowQuantity)).setText("Quantity: " + quantity);
        ((TextView) findViewById(R.id.txtDeliveryAddress))
                .setText(DataHelper.getUserAddress());

        calculateTotals(material, quantity);

        findViewById(R.id.btnConfirmOrder).setOnClickListener(v -> {
            saveBuyNowToFirestore();
        });
    }

    // 🔥 SAVE ORDER TO FIRESTORE
    private void saveBuyNowToFirestore() {

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();

        // Step 1: Create buyNow document
        var buyNowRef = db.collection("users")
                .document(userId)
                .collection("buy_now")
                .document(); // auto ID

        // Optional: you can store order level data here
        Map<String, Object> orderMeta = new HashMap<>();
        orderMeta.put("createdAt", FieldValue.serverTimestamp());

        buyNowRef.set(orderMeta)
                .addOnSuccessListener(unused -> {

                    // Step 2: Add item inside items subcollection
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("materialId", material.id);
                    itemMap.put("quantity", quantity);
                    itemMap.put("createdAt", FieldValue.serverTimestamp());

                    buyNowRef.collection("items")
                            .add(itemMap)
                            .addOnSuccessListener(doc -> {
                                Toast.makeText(this,
                                        "Order Confirmed!",
                                        Toast.LENGTH_SHORT).show();

                                finish();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this,
                                            "Item Save Failed: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show());

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Order Save Failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show());
    }

    private void calculateTotals(Material material, int quantity) {

        double totalMrp = 0;

        String priceString = material.price
                .replaceAll("[₹,]", "")
                .split("/")[0]
                .trim();

        try {
            totalMrp = Double.parseDouble(priceString) * quantity;
        } catch (NumberFormatException ignored) {}

        double discount = totalMrp * 0.1;
        double totalAmount = totalMrp - discount;

        ((TextView) findViewById(R.id.txtTotalMrp))
                .setText(String.format("₹%.2f", totalMrp));

        ((TextView) findViewById(R.id.txtDiscount))
                .setText(String.format("-₹%.2f", discount));

        ((TextView) findViewById(R.id.txtTotalAmount))
                .setText(String.format("₹%.2f", totalAmount));
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
