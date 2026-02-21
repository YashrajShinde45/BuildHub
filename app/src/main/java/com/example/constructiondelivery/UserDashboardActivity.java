package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserDashboardActivity extends BaseActivity implements MaterialAdapter.OnMaterialClickListener {

    RecyclerView recyclerView;
    List<Material> allMaterials;
    List<Material> filteredList;
    MaterialAdapter adapter;
    EditText etSearch;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_user_dashboard);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerMaterials);
        etSearch = findViewById(R.id.etSearch);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        allMaterials = new ArrayList<>();
        filteredList = new ArrayList<>();

        adapter = new MaterialAdapter(this, filteredList, this);
        recyclerView.setAdapter(adapter);

        loadAcceptedMaterials();
    }

    // 🔥 LOAD ONLY ACCEPTED PRODUCTS
    private void loadAcceptedMaterials() {

        db.collection("materials")
                .whereEqualTo("status", "Accepted")
                .get()
                .addOnSuccessListener(snapshot -> {

                    allMaterials.clear();
                    filteredList.clear();

                    for (var doc : snapshot) {

                        Material material = new Material();

                        material.id = doc.getId();
                        material.name = doc.getString("name");
                        material.category = doc.getString("category");

                        Double price = doc.getDouble("price");
                        material.price = price != null ? "₹" + price : "₹0";

                        Long qty = doc.getLong("quantity");
                        material.quantity = qty != null ? String.valueOf(qty) : "0";

                        material.quantityUnit = doc.getString("quantityUnit");
                        material.supplier = doc.getString("supplier");
                        material.shortDesc = doc.getString("shortDesc");
                        material.quality = doc.getString("quality");
                        material.details = doc.getString("details");

                        material.imageUrl = doc.getString("image");

                        allMaterials.add(material);
                    }

                    filteredList.addAll(allMaterials);
                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onMaterialClick(Material material) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("material", material);
        startActivity(intent);
    }
}