package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class CartActivity extends BaseActivity implements CartAdapter.OnCartItemChangedListener {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> cartItems;
    private View emptyCartView;
    private TextView totalPriceTextView;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        emptyCartView = findViewById(R.id.emptyCartView);
        recyclerView = findViewById(R.id.recyclerCartItems);
        totalPriceTextView = findViewById(R.id.totalPrice);
        btnContinue = findViewById(R.id.btnContinue);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = CartManager.getCartItems();
        adapter = new CartAdapter(this, cartItems, this);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnKeepShopping).setOnClickListener(v -> {
            Intent intent = new Intent(this, UserDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, OrderSummaryActivity.class);
            intent.putExtra("cartItems", (Serializable) cartItems);
            startActivity(intent);
        });

        updateCartView();
        updateTotalPrice();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        updateCartView();
        updateTotalPrice();
    }

    @Override
    public void onItemRemoved(CartItem item) {
        CartManager.removeItem(item);
        adapter.notifyDataSetChanged();
        updateCartView();
        updateTotalPrice();
    }

    @Override
    public void onQuantityChanged() {
        adapter.notifyDataSetChanged();
        updateCartView();
        updateTotalPrice();
    }

    private void updateCartView() {
        if (cartItems.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyCartView.setVisibility(View.VISIBLE);
            totalPriceTextView.setVisibility(View.GONE);
            btnContinue.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyCartView.setVisibility(View.GONE);
            totalPriceTextView.setVisibility(View.VISIBLE);
            btnContinue.setVisibility(View.VISIBLE);
        }
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
        totalPriceTextView.setText(String.format("Total: ₹%.2f", total));
    }

    @Override
    protected void setupUniversalNavigation() {
        super.setupUniversalNavigation();

        View btnCart = findViewById(R.id.btnCart);
        if (btnCart != null) {
            btnCart.setVisibility(View.GONE);
        }

        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setVisibility(View.VISIBLE);
        }
    }
}
