package com.example.constructiondelivery;

import android.content.Intent;
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

        // ✅ Admin Button Actions
        btnPlaced.setOnClickListener(v -> updateStatus("Placed"));
        btnShipped.setOnClickListener(v -> updateStatus("Shipped"));
        btnOutForDelivery.setOnClickListener(v -> updateStatus("Out for Delivery"));
    }

    private void updateStatus(String newStatus) {

        if (order == null) return;

        // 1️⃣ Update Global Orders
        db.collection("orders")
                .document(order.orderId)
                .update("orderStatus", newStatus);

        // 2️⃣ Update User Orders
        db.collection("users")
                .document(order.userId)
                .collection("orders")
                .document(order.orderId)
                .update("orderStatus", newStatus)
                .addOnSuccessListener(unused -> {

                    status.setText("Status: " + newStatus);
                    Toast.makeText(this, "Status Updated", Toast.LENGTH_SHORT).show();

                    // ✅ Send Email only when Out for Delivery
                    if (newStatus.equals("Out for Delivery")) {

                        db.collection("users")
                                .document(order.userId)
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {

                                    if (documentSnapshot.exists()) {

                                        String userEmail = documentSnapshot.getString("email");
                                        String userName = documentSnapshot.getString("fullName");

                                        if (userEmail != null && !userEmail.isEmpty()) {
                                            sendEmail(userEmail, userName);
                                        } else {
                                            Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show());
    }

    // ✅ Manual Email Intent
    private void sendEmail(String userEmail, String userName) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{userEmail});
        intent.putExtra(Intent.EXTRA_SUBJECT, "🚚 Your Order is Out for Delivery!");

        String message =
                "Hello " + (userName != null ? userName : "Customer") + ",\n\n" +
                        "Good News! 🎉\n\n" +
                        "Your order is now OUT FOR DELIVERY.\n\n" +
                        "Order Details:\n" +
                        "----------------------------------\n" +
                        "Order ID: " + order.orderId + "\n" +
                        "Total Amount: ₹" + order.totalPrice + "\n" +
                        "Delivery Address: " + order.shippingAddress + "\n" +
                        "Estimated Delivery: Today before 8 PM\n" +
                        "----------------------------------\n\n" +
                        "Our delivery partner is on the way.\n\n" +
                        "Thank you for choosing BuildHub\n\n" +
                        "Best Regards,\n" +
                        "- Team BuildHub";

        intent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(intent, "Send Email"));
        } catch (Exception e) {
            Toast.makeText(this, "No Email App Found", Toast.LENGTH_SHORT).show();
        }
    }
}