package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        profileName = findViewById(R.id.profile_name);

        loadUserName();

        // --- Link to all pages ---
        findViewById(R.id.profileSection)
                .setOnClickListener(v -> startActivity(new Intent(this, EditProfileActivity.class)));

        findViewById(R.id.card_my_orders)
                .setOnClickListener(v -> startActivity(new Intent(this, OrdersActivity.class)));

        findViewById(R.id.card_help_center)
                .setOnClickListener(v -> startActivity(new Intent(this, HelpCenterActivity.class)));

        findViewById(R.id.btn_settings)
                .setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        findViewById(R.id.btn_become_supplier)
                .setOnClickListener(v -> startActivity(new Intent(this, SupplierLoginActivity.class)));

        findViewById(R.id.btn_rate_app)
                .setOnClickListener(v -> startActivity(new Intent(this, RateAppActivity.class)));
    }

    // 🔥 LOAD USER NAME FROM FIRESTORE
    private void loadUserName() {

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            profileName.setText("Hello, User");
            return;
        }

        String userId = currentUser.getUid();

        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {

                        String fullName = documentSnapshot.getString("fullName");

                        if (fullName != null && !fullName.isEmpty()) {
                            profileName.setText("Hello, " + fullName);
                        } else {
                            profileName.setText("Hello, User");
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Failed to load profile",
                                Toast.LENGTH_SHORT).show());
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
