package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;
import java.util.List;

public class AddressActivity extends BaseActivity {

    private EditText etFullName, etAddressLine1, etAddressLine2, etCity, etState, etZipCode, etPhoneNumber;
    private List<CartItem> cartItems;
    private Material material;
    private int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        View back = findViewById(R.id.btnBack);
        if (back != null) {
            back.setVisibility(View.VISIBLE);
        }

        cartItems = (List<CartItem>) getIntent().getSerializableExtra("cartItems");
        if (cartItems == null) {
            material = (Material) getIntent().getSerializableExtra("material");
            quantity = getIntent().getIntExtra("quantity", 1);
        }

        etFullName = findViewById(R.id.etFullName);
        etAddressLine1 = findViewById(R.id.etAddressLine1);
        etAddressLine2 = findViewById(R.id.etAddressLine2);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        etZipCode = findViewById(R.id.etZipCode);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);

        Button btnProceedToPayment = findViewById(R.id.btnProceedToPayment);
        btnProceedToPayment.setOnClickListener(v -> {
            if (validateAddress()) {
                String address = etFullName.getText().toString().trim() + "\n" +
                                 etAddressLine1.getText().toString().trim() + "\n" +
                                 (etAddressLine2.getText().toString().trim().isEmpty() ? "" : etAddressLine2.getText().toString().trim() + "\n") +
                                 etCity.getText().toString().trim() + ", " +
                                 etState.getText().toString().trim() + " - " +
                                 etZipCode.getText().toString().trim() + "\n" +
                                 "Phone: " + etPhoneNumber.getText().toString().trim();

                 Intent intent = new Intent(AddressActivity.this, OrderSummaryActivity.class);
                if (cartItems != null) {
                    intent.putExtra("cartItems", (Serializable) cartItems);
                } else {
                    intent.putExtra("material", material);
                    intent.putExtra("quantity", quantity);
                }
                 intent.putExtra("address", address);
                 startActivity(intent);
            }
        });
    }

    private boolean validateAddress() {
        if (etFullName.getText().toString().trim().isEmpty()) {
            etFullName.setError("Full name is required");
            return false;
        }
        if (etAddressLine1.getText().toString().trim().isEmpty()) {
            etAddressLine1.setError("Address line 1 is required");
            return false;
        }
        if (etCity.getText().toString().trim().isEmpty()) {
            etCity.setError("City is required");
            return false;
        }
        if (etState.getText().toString().trim().isEmpty()) {
            etState.setError("State is required");
            return false;
        }
        if (etZipCode.getText().toString().trim().isEmpty()) {
            etZipCode.setError("ZIP code is required");
            return false;
        }
        if (etPhoneNumber.getText().toString().trim().isEmpty()) {
            etPhoneNumber.setError("Phone number is required");
            return false;
        }
        return true;
    }
}
