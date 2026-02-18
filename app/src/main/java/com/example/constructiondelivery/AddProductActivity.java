package com.example.constructiondelivery;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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

    private EditText etName, etCategory, etPrice,
            etDiscount, etQuantity, etQuantityUnit,
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
        etCategory = findViewById(R.id.etCategory);
        etPrice = findViewById(R.id.etProductPrice);
        etDiscount = findViewById(R.id.etDiscount);
        etQuantity = findViewById(R.id.etQuantity);
        etQuantityUnit = findViewById(R.id.etProductQuantityUnit);
        etSupplier = findViewById(R.id.etSupplier);
        etShortDesc = findViewById(R.id.etProductShortDesc);
        etQuality = findViewById(R.id.etProductQuality);
        etDetails = findViewById(R.id.etProductDetails);

        setupTabs();
        setupHistoryRecycler();

        findViewById(R.id.btnSaveProduct)
                .setOnClickListener(v -> saveProduct());
    }

    private void saveProduct() {

        if (supplierId == null) {
            Toast.makeText(this, "Supplier not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = etName.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String discountStr = etDiscount.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String quantityUnit = etQuantityUnit.getText().toString().trim();
        String supplierName = etSupplier.getText().toString().trim();
        String shortDesc = etShortDesc.getText().toString().trim();
        String quality = etQuality.getText().toString().trim();
        String fullDetails = etDetails.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || quantityUnit.isEmpty()) {
            Toast.makeText(this, "Fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr.replaceAll("[^\\d.]", ""));
        int discount = discountStr.isEmpty() ? 0 : Integer.parseInt(discountStr);
        int quantity = quantityStr.isEmpty() ? 0 : Integer.parseInt(quantityStr);

        Timestamp now = Timestamp.now();

        /* ===============================
           🔥 SAVE TO SUPPLIER PRODUCTS
        =============================== */

        Map<String, Object> supplierMap = new HashMap<>();
        supplierMap.put("productName", name);
        supplierMap.put("price", price);
        supplierMap.put("quantityUnit", quantityUnit);
        supplierMap.put("supplierName", supplierName);
        supplierMap.put("shortDesc", shortDesc);
        supplierMap.put("quality", quality);
        supplierMap.put("fullDetails", fullDetails);
        supplierMap.put("status", "Pending");
        supplierMap.put("productImage", "");
        supplierMap.put("createdAt", now);
        supplierMap.put("updatedAt", now);

        db.collection("suppliers")
                .document(supplierId)
                .collection("products")
                .add(supplierMap)
                .addOnSuccessListener(documentReference -> {

                    String productId = documentReference.getId();
                    addToHistory(name, "Pending", productId);

                    /* ===============================
                       🔥 ALSO SAVE TO MATERIALS
                    =============================== */

                    Map<String, Object> materialMap = new HashMap<>();
                    materialMap.put("name", name);
                    materialMap.put("category", category);
                    materialMap.put("price", price);
                    materialMap.put("discount", discount);
                    materialMap.put("quantity", quantity);
                    materialMap.put("quantityUnit", quantityUnit);
                    materialMap.put("supplier", supplierName);
                    materialMap.put("shortDesc", shortDesc);
                    materialMap.put("quality", quality);
                    materialMap.put("details", fullDetails);
                    materialMap.put("image", "");
                    materialMap.put("status", "Pending");
                    materialMap.put("createdAt", now);
                    materialMap.put("updatedAt", now);

                    db.collection("materials").add(materialMap);

                    Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show();
                    clearFields();
                    tabLayout.getTabAt(1).select();
                });
    }

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

    private void loadProductHistory() {

        productList.clear();
        adapter.notifyDataSetChanged();

        db.collection("suppliers")
                .document(supplierId)
                .collection("product_history")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (var doc : queryDocumentSnapshots) {
                        productList.add(new Product(
                                doc.getString("productName"),
                                doc.getString("status")));
                    }
                    adapter.notifyDataSetChanged();
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
        etCategory.setText("");
        etPrice.setText("");
        etDiscount.setText("");
        etQuantity.setText("");
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
}
