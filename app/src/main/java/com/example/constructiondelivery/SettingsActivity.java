package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // --- Setup Language Spinner ---
        Spinner languageSpinner = findViewById(R.id.spinnerLanguage);
        String[] languages = {"English", "Hindi", "Marathi", "Tamil", "Bengali"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        // --- Add Click Listeners ---
        findViewById(R.id.tvSavedAddress).setOnClickListener(v -> 
            startActivity(new Intent(this, SavedAddressActivity.class)));

        findViewById(R.id.tvBankDetails).setOnClickListener(v -> 
            startActivity(new Intent(this, BankDetailsActivity.class)));

        findViewById(R.id.tvTermsAndConditions).setOnClickListener(v -> 
            Toast.makeText(this, "Displaying Terms and Conditions...", Toast.LENGTH_SHORT).show());

        findViewById(R.id.tvLogout).setOnClickListener(v -> {
            // Clear the cart before signing out
            CartManager.clearLocalCart();
            
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
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
