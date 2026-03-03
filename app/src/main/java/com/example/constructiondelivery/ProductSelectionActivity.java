package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductSelectionActivity extends AppCompatActivity
        implements MaterialAdapter.OnMaterialClickListener {

    private RecyclerView recyclerView;
    private MaterialAdapter adapter;
    private List<Material> materialList;
    private FirebaseFirestore db;

    private String productToExclude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_selection);

        TextView back = findViewById(R.id.btnBack);
        if (back != null) {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(v -> finish());
        }

        recyclerView = findViewById(R.id.recyclerProductSelection);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        materialList = new ArrayList<>();
        adapter = new MaterialAdapter(this, materialList, this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        productToExclude = getIntent().getStringExtra("excludeProduct");

        loadAcceptedProducts();
    }

    private void loadAcceptedProducts() {

        db.collection("materials")
                .whereEqualTo("status", "Accepted")
                .get()
                .addOnSuccessListener(snapshot -> {

                    materialList.clear();

                    for (QueryDocumentSnapshot doc : snapshot) {

                        Material material = new Material();

                        material.productId = doc.getString("productId");
                        material.supplierId = doc.getString("supplierId");
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

                        if (productToExclude != null &&
                                productToExclude.equals(material.name)) {
                            continue;
                        }

                        materialList.add(material);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Failed to load products",
                                Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onMaterialClick(Material material) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selectedMaterial", material);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}