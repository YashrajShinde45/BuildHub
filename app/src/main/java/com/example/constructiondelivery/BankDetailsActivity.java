package com.example.constructiondelivery;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BankDetailsActivity extends BaseActivity {

    private EditText etAccountHolderName, etAccountNumber, etIfscCode, etBankName;
    private Button btnSaveBankDetails;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize Views
        etAccountHolderName = findViewById(R.id.etAccountHolderName);
        etAccountNumber = findViewById(R.id.etAccountNumber);
        etIfscCode = findViewById(R.id.etIfscCode);
        etBankName = findViewById(R.id.etBankName);
        btnSaveBankDetails = findViewById(R.id.btnSaveBankDetails);

        btnSaveBankDetails.setOnClickListener(v -> saveBankDetails());
    }

    private void saveBankDetails() {

        // Safety check
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();

        String accountHolderName = etAccountHolderName.getText().toString().trim();
        String accountNumber = etAccountNumber.getText().toString().trim();
        String ifscCode = etIfscCode.getText().toString().trim();
        String bankName = etBankName.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(accountHolderName)) {
            etAccountHolderName.setError("Required");
            return;
        }

        if (TextUtils.isEmpty(accountNumber)) {
            etAccountNumber.setError("Required");
            return;
        }

        if (TextUtils.isEmpty(ifscCode)) {
            etIfscCode.setError("Required");
            return;
        }

        if (TextUtils.isEmpty(bankName)) {
            etBankName.setError("Required");
            return;
        }

        // Create Bank Map
        Map<String, Object> bankMap = new HashMap<>();
        bankMap.put("accountHolderName", accountHolderName);
        bankMap.put("accountNumber", accountNumber);
        bankMap.put("ifscCode", ifscCode);
        bankMap.put("bankName", bankName);

        // Optional Field
        bankMap.put("verified", false);

        // Save to Firestore
        db.collection("users")
                .document(userId)
                .collection("bank_details")
                .add(bankMap)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(BankDetailsActivity.this,
                            "Bank Details Saved Successfully",
                            Toast.LENGTH_SHORT).show();

                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BankDetailsActivity.this,
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
