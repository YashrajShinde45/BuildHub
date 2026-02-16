package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderSummaryActivity extends AppCompatActivity {

    private TextView deliveryAddress, subtotal, grandTotal;
    private Button payNowButton;
    private RecyclerView recyclerView;
    private OrderSummaryAdapter adapter;
    private List<CartItem> cartItems;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Order Summary");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        deliveryAddress = findViewById(R.id.delivery_address);
        subtotal = findViewById(R.id.subtotal);
        grandTotal = findViewById(R.id.grand_total);
        payNowButton = findViewById(R.id.pay_now_button);
        recyclerView = findViewById(R.id.recyclerOrderSummary);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        payNowButton.setEnabled(false);
        loadSavedAddress();

        Intent intent = getIntent();
        cartItems = (List<CartItem>) intent.getSerializableExtra("cartItems");

        if (cartItems == null) {
            Material material = (Material) intent.getSerializableExtra("material");
            int quantity = intent.getIntExtra("quantity", 1);
            if (material != null) {
                cartItems = new ArrayList<>();
                cartItems.add(new CartItem(material, quantity));
            }
        }

        if (cartItems != null && !cartItems.isEmpty()) {
            adapter = new OrderSummaryAdapter(this, cartItems);
            recyclerView.setAdapter(adapter);
            updateTotalPrice();
        }

        payNowButton.setOnClickListener(v -> {
            Intent paymentIntent = new Intent(OrderSummaryActivity.this, PaymentMethodActivity.class);
            paymentIntent.putExtra("address", address);
            paymentIntent.putExtra("cartItems", (Serializable) cartItems);
            startActivity(paymentIntent);
        });
    }

    private void loadSavedAddress() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to continue.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String userId = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).collection("addresses")
                .whereEqualTo("isDefault", true)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        formatAndSetAddress(document.getData());
                    } else {
                        // No default address found, try to get the latest one
                        db.collection("users").document(userId).collection("addresses")
                                .orderBy("createdAt", Query.Direction.DESCENDING)
                                .limit(1)
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful() && task2.getResult() != null && !task2.getResult().isEmpty()) {
                                        DocumentSnapshot document = task2.getResult().getDocuments().get(0);
                                        formatAndSetAddress(document.getData());
                                    } else {
                                        deliveryAddress.setText("No saved address found. Please add an address in settings.");
                                        payNowButton.setEnabled(false);
                                        Toast.makeText(OrderSummaryActivity.this, "No saved address found.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    private void formatAndSetAddress(Map<String, Object> addressMap) {
        if (addressMap == null) {
            deliveryAddress.setText("No saved address found.");
            payNowButton.setEnabled(false);
            return;
        }

        StringBuilder addressBuilder = new StringBuilder();
        String fullName = (String) addressMap.get("fullName");
        String addressLine1 = (String) addressMap.get("addressLine1");
        String addressLine2 = (String) addressMap.get("addressLine2");
        String city = (String) addressMap.get("city");
        String subDistrict = (String) addressMap.get("subDistrict");
        String district = (String) addressMap.get("district");
        String state = (String) addressMap.get("state");
        String pincode = (String) addressMap.get("pincode");

        if (fullName != null) addressBuilder.append(fullName).append("\n");
        if (addressLine1 != null) addressBuilder.append(addressLine1).append("\n");
        if (addressLine2 != null && !addressLine2.isEmpty()) {
            addressBuilder.append(addressLine2).append("\n");
        }
        if (subDistrict != null && !subDistrict.isEmpty()) {
            addressBuilder.append(subDistrict).append(", ");
        }
        if (city != null) {
            addressBuilder.append(city).append("\n");
        }
        if (district != null && !district.isEmpty()) {
            addressBuilder.append(district).append(", ");
        }
        if (state != null) {
            addressBuilder.append(state).append(" - ");
        }
        if (pincode != null) {
            addressBuilder.append(pincode);
        }

        this.address = addressBuilder.toString();
        deliveryAddress.setText(this.address);
        payNowButton.setEnabled(true);
    }

    private void updateTotalPrice() {
        double total = 0;
        if(cartItems == null) return;
        for (CartItem item : cartItems) {
            try {
                String priceString = item.getMaterial().price.replaceAll("[^0-9.]", "");
                double price = Double.parseDouble(priceString);
                total += price * item.getQuantity();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        subtotal.setText(String.format(Locale.getDefault(), "₹%.2f", total));
        grandTotal.setText(String.format(Locale.getDefault(), "₹%.2f", total)); // Assuming no taxes for now
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
