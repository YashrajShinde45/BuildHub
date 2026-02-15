package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.google.android.material.card.MaterialCardView;

public class CompareActivity extends BaseActivity {

    private static final int SELECT_PRODUCT_1_REQUEST = 1;
    private static final int SELECT_PRODUCT_2_REQUEST = 2;

    private Material product1, product2;

    private MaterialCardView card1, card2;
    private LinearLayout selectionContainer;
    private ProgressBar loadingIndicator;
    private ScrollView resultsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_compare);

        selectionContainer = findViewById(R.id.selectionContainer);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        resultsContainer = findViewById(R.id.resultsContainer);
        card1 = findViewById(R.id.card_product_1);
        card2 = findViewById(R.id.card_product_2);
        Button btnCompare = findViewById(R.id.btnCompare);

        // Make the back button visible for this screen
        View back = findViewById(R.id.btnBack);
        if (back != null) {
            back.setVisibility(View.VISIBLE);
        }

        card1.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductSelectionActivity.class);
            startActivityForResult(intent, SELECT_PRODUCT_1_REQUEST);
        });

        card2.setOnClickListener(v -> {
            if (product1 == null) {
                Toast.makeText(this, "Select the first product.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, ProductSelectionActivity.class);
            intent.putExtra("excludeProduct", product1.name);
            startActivityForResult(intent, SELECT_PRODUCT_2_REQUEST);
        });

        btnCompare.setOnClickListener(v -> {
            if (product1 == null || product2 == null) {
                Toast.makeText(this, "Please select two products to compare.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Start loading simulation
            selectionContainer.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.VISIBLE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // After 2.5 seconds, show results
                loadingIndicator.setVisibility(View.GONE);
                populateComparisonResults();
                resultsContainer.setVisibility(View.VISIBLE);
            }, 2500);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) return;

        Material selectedMaterial = (Material) data.getSerializableExtra("selectedMaterial");
        if (selectedMaterial == null) return;

        if (requestCode == SELECT_PRODUCT_1_REQUEST) {
            product1 = selectedMaterial;
            updateProductCard(card1, product1);
        } else if (requestCode == SELECT_PRODUCT_2_REQUEST) {
            product2 = selectedMaterial;
            updateProductCard(card2, product2);
        }
    }

    private void updateProductCard(MaterialCardView card, Material material) {
        card.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        View productView = inflater.inflate(R.layout.item_compare_product, card, false);

        ImageView img = productView.findViewById(R.id.imgCompareProduct);
        TextView name = productView.findViewById(R.id.txtCompareName);
        TextView price = productView.findViewById(R.id.txtComparePrice);

        img.setImageResource(material.image);
        name.setText(material.name);
        price.setText(material.price);

        card.addView(productView);
    }

    private void populateComparisonResults() {
        // Product 1 Details
        ((ImageView) findViewById(R.id.imgProduct1)).setImageResource(product1.image);
        ((TextView) findViewById(R.id.txtName1)).setText(product1.name);
        ((TextView) findViewById(R.id.txtPrice1)).setText(product1.price);
        ((TextView) findViewById(R.id.txtSupplier1)).setText("Supplier: " + product1.supplier);
        ((TextView) findViewById(R.id.txtDescription1)).setText(product1.shortDesc);
        ((TextView) findViewById(R.id.txtDetails1)).setText(product1.details);

        // Product 2 Details
        ((ImageView) findViewById(R.id.imgProduct2)).setImageResource(product2.image);
        ((TextView) findViewById(R.id.txtName2)).setText(product2.name);
        ((TextView) findViewById(R.id.txtPrice2)).setText(product2.price);
        ((TextView) findViewById(R.id.txtSupplier2)).setText("Supplier: " + product2.supplier);
        ((TextView) findViewById(R.id.txtDescription2)).setText(product2.shortDesc);
        ((TextView) findViewById(R.id.txtDetails2)).setText(product2.details);
    }
}
