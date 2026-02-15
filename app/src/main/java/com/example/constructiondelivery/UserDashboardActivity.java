package com.example.constructiondelivery;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserDashboardActivity extends BaseActivity implements MaterialAdapter.OnMaterialClickListener {

    RecyclerView recyclerView;
    List<Material> allMaterials;
    List<Material> filteredList;
    MaterialAdapter adapter;
    LinearLayout categoryContainer;
    EditText etSearch;

    String[] categories = {
            "All",
            "Natural Materials",
            "Cement & Binding Materials",
            "Bricks & Blocks",
            "Structural Materials",
            "Finishing Materials",
            "Roofing Materials",
            "Plumbing & Sanitary Materials",
            "Electrical Materials",
            "Insulation & Waterproofing"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_user_dashboard);

        recyclerView = findViewById(R.id.recyclerMaterials);
        categoryContainer = findViewById(R.id.categoryContainer);
        etSearch = findViewById(R.id.etSearch);

        findViewById(R.id.btnSearch).setOnClickListener(v -> searchMaterials());

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Use the centralized data source
        allMaterials = DataHelper.getAllMaterials();
        filteredList = new ArrayList<>();
        adapter = new MaterialAdapter(this, filteredList, this);
        recyclerView.setAdapter(adapter);

        setupCategories();
        showAllMaterials();
    }

    private void searchMaterials() {
        String query = etSearch.getText().toString().toLowerCase().trim();
        if (query.isEmpty()) {
            showAllMaterials();
            return;
        }

        filteredList.clear();
        for (Material material : allMaterials) {
            if (material.name.toLowerCase().contains(query) || material.category.toLowerCase().contains(query)) {
                filteredList.add(material);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void setupCategories() {
        for (String cat : categories) {
            Button btn = new Button(this);
            btn.setText(cat);
            btn.setAllCaps(false);
            btn.setTextColor(Color.BLACK);

            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(25);
            shape.setColor(Color.parseColor("#FFE0B2"));
            btn.setBackground(shape);

            btn.setPadding(30, 15, 30, 15);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(16, 12, 16, 12);
            btn.setLayoutParams(params);

            btn.setOnClickListener(v -> filterByCategory(cat));
            categoryContainer.addView(btn);
        }
    }

    private void filterByCategory(String category) {
        etSearch.setText(""); // Clear search when category is clicked
        filteredList.clear();

        if (category.equals("All")) {
            filteredList.addAll(allMaterials);
        } else {
            for (Material m : allMaterials) {
                if (m.category.equalsIgnoreCase(category)) {
                    filteredList.add(m);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void showAllMaterials() {
        filteredList.clear();
        filteredList.addAll(allMaterials);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMaterialClick(Material material) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("material", material);
        startActivity(intent);
    }
}
