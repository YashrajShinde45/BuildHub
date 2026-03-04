package com.example.constructiondelivery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
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

    private static final int PICK_IMAGE = 1;

    private Uri imageUri;
    private ImageView imgPreview;

    private ScrollView addProductContainer;
    private RecyclerView historyContainer;
    private TabLayout tabLayout;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String supplierId;

    private EditText etName, etCategory, etPrice,
            etQuantity, etQuantityUnit,
            etSupplier, etShortDesc, etQuality,
            etDetails;

    private HistoryAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Cloudinary
        CloudinaryManager.init(this);

        setContentView(R.layout.activity_add_product);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            supplierId = mAuth.getCurrentUser().getUid();
        }

        imgPreview = findViewById(R.id.imgPreview);

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

        setupHistory();
        setupTabs();

        findViewById(R.id.btnSelectImage).setOnClickListener(v -> selectImage());

        findViewById(R.id.btnSaveProduct).setOnClickListener(v -> uploadImageAndSave());
    }

    private void selectImage(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){

            imageUri=data.getData();

            imgPreview.setImageURI(imageUri);
        }
    }

    private void uploadImageAndSave(){

        if(imageUri==null){
            Toast.makeText(this,"Select Image",Toast.LENGTH_SHORT).show();
            return;
        }

        MediaManager.get().upload(imageUri)

                .unsigned("buildhub_upload")

                .callback(new UploadCallback() {

                    @Override
                    public void onStart(String requestId) {}

                    @Override
                    public void onProgress(String requestId,long bytes,long totalBytes) {}

                    @Override
                    public void onSuccess(String requestId,Map resultData) {

                        String imageUrl=resultData.get("secure_url").toString();

                        saveProduct(imageUrl);

                        Toast.makeText(AddProductActivity.this,
                                "Image Uploaded",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {

                        Toast.makeText(AddProductActivity.this,
                                error.getDescription(),
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onReschedule(String requestId,ErrorInfo error) {}
                })
                .dispatch();
    }

    private void saveProduct(String imageUrl){

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

        // ⭐ IMAGE URL FROM CLOUDINARY
        materialMap.put("imageUrl", imageUrl);

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

        imgPreview.setImageResource(R.drawable.landing_image);
    }

    private void setupTabs() {

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition()==0){

                    addProductContainer.setVisibility(View.VISIBLE);
                    historyContainer.setVisibility(View.GONE);

                }else{

                    addProductContainer.setVisibility(View.GONE);
                    historyContainer.setVisibility(View.VISIBLE);
                    loadHistory();
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupHistory(){

        productList=new ArrayList<>();
        adapter=new HistoryAdapter(productList);

        historyContainer.setLayoutManager(new LinearLayoutManager(this));
        historyContainer.setAdapter(adapter);
    }

    private void loadHistory(){

        if (supplierId == null) return;

        productList.clear();

        db.collection("suppliers")
                .document(supplierId)
                .collection("product_history")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snapshot->{

                    snapshot.forEach(doc->{

                        productList.add(new Product(
                                doc.getString("productName"),
                                doc.getString("status")
                        ));
                    });

                    adapter.notifyDataSetChanged();
                });
    }
}