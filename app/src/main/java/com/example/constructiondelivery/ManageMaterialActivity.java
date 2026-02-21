package com.example.constructiondelivery;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageMaterialActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ManageMaterialAdapter adapter;
    private List<Material> materialList;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_material);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        materialList = new ArrayList<>();
        adapter = new ManageMaterialAdapter(this, materialList);
        recyclerView.setAdapter(adapter);

        loadPendingMaterials();
    }

    private void loadPendingMaterials() {

        db.collection("materials")
                .whereEqualTo("status", "Pending")   // 🔥 VERY IMPORTANT
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    materialList.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

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
                        material.status = doc.getString("status");

                        materialList.add(material);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Failed to load materials",
                                Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}