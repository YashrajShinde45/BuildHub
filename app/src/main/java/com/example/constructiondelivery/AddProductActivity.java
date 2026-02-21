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
    private TabLayout tabLayout;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String supplierId;

    private EditText etName, etCategory, etPrice,
            etQuantity, etQuantityUnit,
            etSupplier, etShortDesc, etQuality,
            etDetails, etImageUrl;

    private HistoryAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

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
        etQuantity = findViewById(R.id.etQuantity);
        etQuantityUnit = findViewById(R.id.etProductQuantityUnit);
        etSupplier = findViewById(R.id.etSupplier);
        etShortDesc = findViewById(R.id.etProductShortDesc);
        etQuality = findViewById(R.id.etProductQuality);
        etDetails = findViewById(R.id.etProductDetails);
        etImageUrl = findViewById(R.id.etImageUrl);

        setupHistory();
        setupTabs();

        findViewById(R.id.btnSaveProduct)
                .setOnClickListener(v -> saveProduct());
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
                    loadHistory();
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupHistory() {
        productList = new ArrayList<>();
        adapter = new HistoryAdapter(productList);
        historyContainer.setLayoutManager(new LinearLayoutManager(this));
        historyContainer.setAdapter(adapter);
    }

    private void loadHistory() {
        if (supplierId == null) return;

        productList.clear();

        db.collection("suppliers")
                .document(supplierId)
                .collection("product_history")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snapshot -> {

                    for (var doc : snapshot) {
                        productList.add(new Product(
                                doc.getString("productName"),
                                doc.getString("status")
                        ));
                    }

                    adapter.notifyDataSetChanged();
                });
    }

    private void saveProduct() {

        if (supplierId == null) {
            Toast.makeText(this, "Supplier not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = etName.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String quantityUnit = etQuantityUnit.getText().toString().trim();
        String supplierName = etSupplier.getText().toString().trim();
        String shortDesc = etShortDesc.getText().toString().trim();
        String quality = etQuality.getText().toString().trim();
        String fullDetails = etDetails.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        int quantity = quantityStr.isEmpty() ? 0 : Integer.parseInt(quantityStr);

        Timestamp now = Timestamp.now();

        String productId = db.collection("materials").document().getId();

        Map<String, Object> materialMap = new HashMap<>();
        materialMap.put("productId", productId);
        materialMap.put("name", name);
        materialMap.put("category", category);
        materialMap.put("price", price);
        materialMap.put("quantity", quantity);
        materialMap.put("quantityUnit", quantityUnit);
        materialMap.put("supplier", supplierName);
        materialMap.put("supplierId", supplierId);
        materialMap.put("shortDesc", shortDesc);
        materialMap.put("quality", quality);
        materialMap.put("details", fullDetails);
        materialMap.put("image", imageUrl);
        materialMap.put("status", "Pending");
        materialMap.put("createdAt", now);

        db.collection("materials")
                .document(productId)
                .set(materialMap);

        Map<String, Object> historyMap = new HashMap<>();
        historyMap.put("productId", productId);
        historyMap.put("productName", name);
        historyMap.put("status", "Pending");
        historyMap.put("createdAt", now);

        db.collection("suppliers")
                .document(supplierId)
                .collection("product_history")
                .document(productId)
                .set(historyMap);

        Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    private void clearFields() {
        etName.setText("");
        etCategory.setText("");
        etPrice.setText("");
        etQuantity.setText("");
        etQuantityUnit.setText("");
        etSupplier.setText("");
        etShortDesc.setText("");
        etQuality.setText("");
        etDetails.setText("");
        etImageUrl.setText("");
    }
}