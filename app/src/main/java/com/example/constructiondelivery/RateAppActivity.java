package com.example.constructiondelivery;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RateAppActivity extends BaseActivity {

    private RatingBar ratingBar;
    private EditText etFeedback;
    private Button btnSubmit;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_app);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ratingBar = findViewById(R.id.ratingBar);
        etFeedback = findViewById(R.id.etFeedback);
        btnSubmit = findViewById(R.id.btnSubmitFeedback);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnSubmit.setOnClickListener(v -> saveRating());
    }

    private void saveRating() {

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        float ratingValue = ratingBar.getRating();
        String feedback = etFeedback.getText().toString().trim();

        if (ratingValue == 0) {
            Toast.makeText(this, "Please give a rating", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(feedback)) {
            etFeedback.setError("Please enter feedback");
            return;
        }

        Map<String, Object> ratingMap = new HashMap<>();
        ratingMap.put("rating", ratingValue);
        ratingMap.put("feedback", feedback);
        ratingMap.put("createdAt", FieldValue.serverTimestamp());

        db.collection("users")
                .document(userId)
                .collection("ratings")
                .add(ratingMap)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(this,
                            "Thanks for your feedback!",
                            Toast.LENGTH_SHORT).show();

                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show());
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
