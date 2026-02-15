package com.example.constructiondelivery;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

public class RateAppActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_app);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button submitButton = findViewById(R.id.btnSubmitFeedback);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        submitButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            Toast.makeText(this, "Thanks for your feedback! Rating: " + rating, Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
