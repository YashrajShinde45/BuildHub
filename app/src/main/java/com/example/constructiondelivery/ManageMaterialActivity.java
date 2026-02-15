package com.example.constructiondelivery;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ManageMaterialActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ManageMaterialAdapter adapter;
    private List<Material> materialList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_material);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dummy data for now
        materialList = new ArrayList<>();
        materialList.add(new Material("Cement", "Building Materials", "₹500/bag", "100", "bags", "Supplier A", "High quality cement", "OPC 53 Grade", "", 0));
        materialList.add(new Material("Steel Rods", "Building Materials", "₹60/kg", "500", "kgs", "Supplier B", "TMT Steel rods", "Fe 500D", "", 0));

        adapter = new ManageMaterialAdapter(this, materialList);
        recyclerView.setAdapter(adapter);
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
