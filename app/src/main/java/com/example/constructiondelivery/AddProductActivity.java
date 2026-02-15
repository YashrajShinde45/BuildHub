package com.example.constructiondelivery;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends BaseActivity {

    private ScrollView addProductContainer;
    private RecyclerView historyContainer;
    private HistoryAdapter adapter;
    private List<Product> productList;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        addProductContainer = findViewById(R.id.addProductContainer);
        historyContainer = findViewById(R.id.rvHistory);
        tabLayout = findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    addProductContainer.setVisibility(View.VISIBLE);
                    historyContainer.setVisibility(View.GONE);
                } else {
                    addProductContainer.setVisibility(View.GONE);
                    historyContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // --- Setup History RecyclerView ---
        productList = new ArrayList<>();
        productList.add(new Product("Cement", "Approved"));
        productList.add(new Product("Bricks", "Pending"));
        productList.add(new Product("Sand", "Approved"));

        adapter = new HistoryAdapter(productList);
        historyContainer.setLayoutManager(new LinearLayoutManager(this));
        historyContainer.setAdapter(adapter);

        // --- Find all EditTexts ---
        EditText etName = findViewById(R.id.etProductName);
        EditText etPrice = findViewById(R.id.etProductPrice);
        EditText etQuantityUnit = findViewById(R.id.etProductQuantityUnit);
        EditText etSupplier = findViewById(R.id.etProductSupplier);
        EditText etShortDesc = findViewById(R.id.etProductShortDesc);
        EditText etQuality = findViewById(R.id.etProductQuality);
        EditText etDetails = findViewById(R.id.etProductDetails);

        // --- Save Button Logic ---
        findViewById(R.id.btnSaveProduct).setOnClickListener(v -> {
            String name = etName.getText().toString();
            String price = etPrice.getText().toString();
            String quantityUnit = etQuantityUnit.getText().toString();
            String supplier = etSupplier.getText().toString();

            if (name.isEmpty() || price.isEmpty() || quantityUnit.isEmpty() || supplier.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add the new product to the history list with "Pending" status
            productList.add(new Product(name, "Pending"));
            adapter.notifyDataSetChanged();

            Toast.makeText(this, "Product Added: " + name, Toast.LENGTH_SHORT).show();

            // Clear the input fields
            etName.setText("");
            etPrice.setText("");
            etQuantityUnit.setText("");
            etSupplier.setText("");
            etShortDesc.setText("");
            etQuality.setText("");
            etDetails.setText("");
            
            // Switch to the history tab
            tabLayout.getTabAt(1).select();
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
