package com.example.constructiondelivery;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SavedAddressActivity extends BaseActivity {

    private EditText etFullName, etAddressLine1, etAddressLine2,
            etCity, etSubDistrict, etDistrict, etState, etPincode;
    private Button btnSaveAddress;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_address);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize Views
        etFullName = findViewById(R.id.etFullName);
        etAddressLine1 = findViewById(R.id.etAddressLine1);
        etAddressLine2 = findViewById(R.id.etAddressLine2);
        etCity = findViewById(R.id.etCity);
        etSubDistrict = findViewById(R.id.etSubDistrict);
        etDistrict = findViewById(R.id.etDistrict);
        etState = findViewById(R.id.etState);
        etPincode = findViewById(R.id.etPincode);
        btnSaveAddress = findViewById(R.id.btnSaveAddress);

        btnSaveAddress.setOnClickListener(v -> saveAddress());
    }

    private void saveAddress() {

        String userId = mAuth.getCurrentUser().getUid();

        String fullName = etFullName.getText().toString().trim();
        String addressLine1 = etAddressLine1.getText().toString().trim();
        String addressLine2 = etAddressLine2.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String subDistrict = etSubDistrict.getText().toString().trim();
        String district = etDistrict.getText().toString().trim();
        String state = etState.getText().toString().trim();
        String pincode = etPincode.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(addressLine1)) {
            etAddressLine1.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(addressLine2)) {
            etAddressLine2.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(city)) {
            etCity.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(subDistrict)) {
            etSubDistrict.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(district)) {
            etDistrict.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(state)) {
            etState.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(pincode)) {
            etPincode.setError("Required");
            return;
        }

        // Create Address Map
        Map<String, Object> addressMap = new HashMap<>();
        addressMap.put("fullName", fullName);
        addressMap.put("addressLine1", addressLine1);
        addressMap.put("addressLine2", addressLine2);
        addressMap.put("city", city);
        addressMap.put("subDistrict", subDistrict);
        addressMap.put("district", district);
        addressMap.put("state", state);
        addressMap.put("pincode", pincode);

        // Optional Fields
        addressMap.put("isDefault", true); // change logic later if needed
        addressMap.put("createdAt", FieldValue.serverTimestamp());

        // Save to Firestore
        db.collection("users")
                .document(userId)
                .collection("addresses")
                .add(addressMap)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(SavedAddressActivity.this,
                            "Address Saved Successfully",
                            Toast.LENGTH_SHORT).show();

                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SavedAddressActivity.this,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
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
