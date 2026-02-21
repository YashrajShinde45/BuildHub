package com.example.constructiondelivery;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView orderId, userId, totalPrice, address, status;
    private Button btnPlaced, btnShipped, btnOutForDelivery;

    private FirebaseFirestore db;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        db = FirebaseFirestore.getInstance();

        order = (Order) getIntent().getSerializableExtra("order");

        orderId = findViewById(R.id.detail_order_id);
        userId = findViewById(R.id.detail_user_id);
        totalPrice = findViewById(R.id.detail_total_price);
        address = findViewById(R.id.detail_address);
        status = findViewById(R.id.detail_status);

        btnPlaced = findViewById(R.id.btnPlaced);
        btnShipped = findViewById(R.id.btnShipped);
        btnOutForDelivery = findViewById(R.id.btnOutForDelivery);

        if (order != null) {
            orderId.setText("Order ID: " + order.orderId);
            userId.setText("User ID: " + order.userId);
            totalPrice.setText("Total: " + order.totalPrice);
            address.setText("Address: " + order.shippingAddress);
            status.setText("Status: " + order.orderStatus);
        }

        btnPlaced.setOnClickListener(v -> updateStatus("Placed"));
        btnShipped.setOnClickListener(v -> updateStatus("Shipped"));
        btnOutForDelivery.setOnClickListener(v -> updateStatus("Out for Delivery"));
    }

    private void updateStatus(String newStatus) {

        if (order == null) return;

        db.collection("orders")
                .document(order.orderId)
                .update("orderStatus", newStatus)
                .addOnSuccessListener(unused -> {
                    status.setText("Status: " + newStatus);
                    Toast.makeText(this, "Status updated", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show());
    }
}