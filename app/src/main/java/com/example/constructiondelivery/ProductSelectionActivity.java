package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductSelectionActivity extends AppCompatActivity implements MaterialAdapter.OnMaterialClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_selection);

        // --- BACK BUTTON LOGIC ---
        TextView back = findViewById(R.id.btnBack);
        if (back != null) {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(v -> finish()); // Simply close the activity
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerProductSelection);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        List<Material> allMaterials = DataHelper.getAllMaterials();
        String productToExclude = getIntent().getStringExtra("excludeProduct");

        if (productToExclude != null) {
            List<Material> filteredMaterials = new ArrayList<>();
            for (Material m : allMaterials) {
                if (!m.name.equals(productToExclude)) {
                    filteredMaterials.add(m);
                }
            }
            allMaterials = filteredMaterials;
        }

        MaterialAdapter adapter = new MaterialAdapter(this, allMaterials, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onMaterialClick(Material material) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selectedMaterial", material);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
