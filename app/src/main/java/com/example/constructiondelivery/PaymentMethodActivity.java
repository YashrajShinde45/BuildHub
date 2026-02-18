package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PaymentMethodActivity extends AppCompatActivity {

    private RadioGroup paymentMethodGroup;
    private TextInputLayout transactionIdLayout;
    private EditText transactionIdEditText;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private List<CartItem> cartItems;
    private String shippingAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        paymentMethodGroup = findViewById(R.id.payment_method_group);
        transactionIdLayout = findViewById(R.id.transaction_id_layout);
        transactionIdEditText = findViewById(R.id.transaction_id_edit_text);
        Button placeOrderButton = findViewById(R.id.place_order_button);

        shippingAddress = getIntent().getStringExtra("address");
        cartItems = (List<CartItem>) getIntent().getSerializableExtra("cartItems");

        transactionIdLayout.setVisibility(View.GONE);

        paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.pay_online_radio_button) {
                transactionIdLayout.setVisibility(View.VISIBLE);
            } else {
                transactionIdLayout.setVisibility(View.GONE);
            }
        });

        placeOrderButton.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(this, "No items found", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPaymentMethodId = paymentMethodGroup.getCheckedRadioButtonId();

        if (selectedPaymentMethodId == -1) {
            Toast.makeText(this, "Select payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        String paymentMethod;
        String transactionId = "";
        String paymentStatus;

        if (selectedPaymentMethodId == R.id.pay_online_radio_button) {
            paymentMethod = "ONLINE";
            transactionId = transactionIdEditText.getText().toString().trim();

            if (transactionId.isEmpty()) {
                transactionIdEditText.setError("Transaction ID required");
                return;
            }

            paymentStatus = "Success";

        } else {
            paymentMethod = "COD";
            paymentStatus = "Pending";
        }

        String userId = mAuth.getCurrentUser().getUid();

        // 🔥 Calculate Total
        double total = 0;
        for (CartItem item : cartItems) {
            try {
                String priceString = item.getMaterial().price.replaceAll("[^0-9.]", "");
                double price = Double.parseDouble(priceString);
                total += price * item.getQuantity();
            } catch (Exception ignored) {}
        }

        String totalPrice = String.format(Locale.getDefault(), "₹%.2f", total);

        /* ---------------------------------------------------------
           1️⃣ SAVE IN BUY_NOW COLLECTION (UNCHANGED)
        --------------------------------------------------------- */

        var buyNowRef = db.collection("users")
                .document(userId)
                .collection("buy_now")
                .document();

        Map<String, Object> buyNowData = new HashMap<>();
        buyNowData.put("paymentMethod", paymentMethod);
        buyNowData.put("paymentStatus", paymentStatus);
        buyNowData.put("transactionId", transactionId);
        buyNowData.put("totalAmount", totalPrice);
        buyNowData.put("orderStatus", "Placed");
        buyNowData.put("createdAt", FieldValue.serverTimestamp());

        buyNowRef.set(buyNowData).addOnSuccessListener(unused -> {
            for (CartItem item : cartItems) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("materialId", item.getMaterial().id);
                itemMap.put("quantity", item.getQuantity());
                itemMap.put("createdAt", FieldValue.serverTimestamp());
                buyNowRef.collection("items").add(itemMap);
            }
        });

        /* ---------------------------------------------------------
           2️⃣ SAVE IN USER ORDERS COLLECTION (UNCHANGED)
        --------------------------------------------------------- */

        var orderRef = db.collection("users")
                .document(userId)
                .collection("orders")
                .document();

        String orderId = orderRef.getId();

        Map<String, Object> orderData = new HashMap<>();
        orderData.put("orderId", orderId);
        orderData.put("userId", userId);
        orderData.put("totalPrice", totalPrice);
        orderData.put("shippingAddress", shippingAddress);
        orderData.put("orderStatus", "Placed");
        orderData.put("paymentMethod", paymentMethod);
        orderData.put("transactionId", transactionId);
        orderData.put("orderDate", FieldValue.serverTimestamp());

        orderRef.set(orderData).addOnSuccessListener(unused -> {

            for (CartItem item : cartItems) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("materialId", item.getMaterial().id);
                itemMap.put("quantity", item.getQuantity());
                orderRef.collection("items").add(itemMap);
            }

            /* ---------------------------------------------------------
               3️⃣ 🔥 SAVE IN GLOBAL ORDERS COLLECTION (NEW)
            --------------------------------------------------------- */

            var globalOrderRef = db.collection("orders").document(orderId);

            globalOrderRef.set(orderData).addOnSuccessListener(unused2 -> {

                for (CartItem item : cartItems) {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("materialId", item.getMaterial().id);
                    itemMap.put("quantity", item.getQuantity());
                    globalOrderRef.collection("items").add(itemMap);
                }

                Toast.makeText(this,
                        "Order placed successfully!",
                        Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(
                            PaymentMethodActivity.this,
                            OrdersActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    finish();
                }, 1500);
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
