package com.example.constructiondelivery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ManageMaterialAdapter extends RecyclerView.Adapter<ManageMaterialAdapter.ViewHolder> {

    private Context context;
    private List<Material> materialList;
    private FirebaseFirestore db;

    public ManageMaterialAdapter(Context context, List<Material> materialList) {
        this.context = context;
        this.materialList = materialList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_manage_material, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Material material = materialList.get(position);

        holder.materialName.setText(material.name);
        holder.materialCategory.setText("Category: " + material.category);
        holder.materialPrice.setText("Price: " + material.price);

        /* =========================
           ✅ ACCEPT
        ========================= */

        holder.acceptButton.setOnClickListener(v -> {

            db.collection("materials")
                    .document(material.id)
                    .update("status", "Accepted")
                    .addOnSuccessListener(unused -> {

                        removeFromUI(holder.getAdapterPosition());

                        Toast.makeText(context,
                                "Accepted: " + material.name,
                                Toast.LENGTH_SHORT).show();
                    });
        });

        /* =========================
           ❌ REJECT
        ========================= */

        holder.rejectButton.setOnClickListener(v -> {

            db.collection("materials")
                    .document(material.id)
                    .update("status", "Rejected")
                    .addOnSuccessListener(unused -> {

                        removeFromUI(holder.getAdapterPosition());

                        Toast.makeText(context,
                                "Rejected: " + material.name,
                                Toast.LENGTH_SHORT).show();
                    });
        });
    }

    /* =========================
       🔥 REMOVE FROM UI ONLY
    ========================= */

    private void removeFromUI(int position) {
        if (position != RecyclerView.NO_POSITION) {
            materialList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, materialList.size());
        }
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView materialName, materialCategory, materialPrice;
        Button acceptButton, rejectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            materialName = itemView.findViewById(R.id.material_name);
            materialCategory = itemView.findViewById(R.id.material_category);
            materialPrice = itemView.findViewById(R.id.material_price);
            acceptButton = itemView.findViewById(R.id.btn_accept);
            rejectButton = itemView.findViewById(R.id.btn_reject);
        }
    }
}