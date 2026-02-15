package com.example.constructiondelivery;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class EditProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            Toast.makeText(this, "Profile Saved!", Toast.LENGTH_SHORT).show();
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
