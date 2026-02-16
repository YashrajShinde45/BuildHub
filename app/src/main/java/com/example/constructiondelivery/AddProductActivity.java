package com.example.constructiondelivery;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductActivity extends BaseActivity {

    private ScrollView addProductContainer;
    private RecyclerView historyContainer;
    private HistoryAdapter adapter;
    private List<Product> productList;
    private TabLayout tabLayout;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private EditText etName, etPrice, etQuantityUnit,
            etSupplier, etShortDesc, etQuality, etDetails;

    private String supplierId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            supplierId = mAuth.getCurrentUser().getUid();
        }

        addProductContainer = findViewById(R.id.addProductContainer);
        historyContainer = findViewById(R.id.rvHistory);
        tabLayout = findViewById(R.id.tabLayout);

        etName = findViewById(R.id.etProductName);
        etPrice = findViewById(R.id.etProductPrice);
        etQuantityUnit = findViewById(R.id.etProductQuantityUnit);
        etSupplier = findViewById(R.id.etProductSupplier);
        etShortDesc = findViewById(R.id.etProductShortDesc);
        etQuality = findViewById(R.id.etProductQuality);
        etDetails = findViewById(R.id.etProductDetails);

        setupTabs();
        setupHistoryRecycler();

        findViewById(R.id.btnSaveProduct)
                .setOnClickListener(v -> saveProduct());
    }

    // =========================
    // SAVE PRODUCT
    // =========================
    private void saveProduct() {

        if (supplierId == null) {
            Toast.makeText(this, "Supplier not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = etName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String quantityUnit = etQuantityUnit.getText().toString().trim();
        String supplierName = etSupplier.getText().toString().trim();
        String shortDesc = etShortDesc.getText().toString().trim();
        String quality = etQuality.getText().toString().trim();
        String fullDetails = etDetails.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() ||
                quantityUnit.isEmpty() || supplierName.isEmpty()) {
            Toast.makeText(this, "Fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr.replaceAll("[^\\d.]", ""));
        } catch (Exception e) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> productMap = new HashMap<>();
        productMap.put("productName", name);
        productMap.put("price", price);
        productMap.put("quantityUnit", quantityUnit);
        productMap.put("supplierName", supplierName);
        productMap.put("shortDesc", shortDesc);
        productMap.put("quality", quality);
        productMap.put("fullDetails", fullDetails);
        productMap.put("status", "Pending");
        productMap.put("productImage", "");
        productMap.put("createdAt", Timestamp.now());
        productMap.put("updatedAt", Timestamp.now());

        db.collection("suppliers")
                .document(supplierId)
                .collection("products")
                .add(productMap)
                .addOnSuccessListener(documentReference -> {

                    String productId = documentReference.getId();

                    // 🔥 ALSO ADD TO PRODUCT HISTORY
                    addToHistory(name, "Pending", productId);

                    Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show();
                    clearFields();
                    tabLayout.getTabAt(1).select();
                });
    }

    // =========================
    // ADD TO HISTORY COLLECTION
    // =========================
    private void addToHistory(String name, String status, String productId) {

        Map<String, Object> historyMap = new HashMap<>();
        historyMap.put("productName", name);
        historyMap.put("status", status);
        historyMap.put("productId", productId);
        historyMap.put("createdAt", Timestamp.now());

        db.collection("suppliers")
                .document(supplierId)
                .collection("product_history")
                .add(historyMap)
                .addOnSuccessListener(doc -> loadProductHistory());
    }

    // =========================
    // LOAD HISTORY FROM FIRESTORE
    // =========================
    private void loadProductHistory() {

        productList.clear();
        adapter.notifyDataSetChanged();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        supplierId = currentUser.getUid();

        db.collection("suppliers")
                .document(supplierId)
                .collection("product_history")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("productName");
                        String status = doc.getString("status");
                        productList.add(new Product(name, status));
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddProductActivity.this, "Failed to load history", Toast.LENGTH_SHORT).show();
                });
    }

    private void setupHistoryRecycler() {
        productList = new ArrayList<>();
        adapter = new HistoryAdapter(productList);
        historyContainer.setLayoutManager(new LinearLayoutManager(this));
        historyContainer.setAdapter(adapter);
    }

    private void clearFields() {
        etName.setText("");
        etPrice.setText("");
        etQuantityUnit.setText("");
        etSupplier.setText("");
        etShortDesc.setText("");
        etQuality.setText("");
        etDetails.setText("");
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    addProductContainer.setVisibility(View.VISIBLE);
                    historyContainer.setVisibility(View.GONE);
                } else {
                    addProductContainer.setVisibility(View.GONE);
                    historyContainer.setVisibility(View.VISIBLE);
                    loadProductHistory();
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    protected void setupUniversalNavigation() {
        super.setupUniversalNavigation();
        View backButton = findViewById(R.id.btnBack);
        if (backButton != null) backButton.setVisibility(View.VISIBLE);
    }
}
