package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderSummaryActivity extends AppCompatActivity {

    private TextView deliveryAddress, subtotal, grandTotal;
    private Button payNowButton;
    private RecyclerView recyclerView;
    private OrderSummaryAdapter adapter;
    private List<CartItem> cartItems;

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

        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        deliveryAddress.setText(address);

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

    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            try {
                String priceString = item.getMaterial().price.replaceAll("[^\\d.]", "");
                double price = Double.parseDouble(priceString);
                total += price * item.getQuantity();
            } catch (NumberFormatException e) {
                // Handle parsing error
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
