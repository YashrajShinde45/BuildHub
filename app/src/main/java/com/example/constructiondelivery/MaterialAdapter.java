package com.example.constructiondelivery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    public interface OnMaterialClickListener {
        void onMaterialClick(Material material);
    }

    private final Context context;
    private final List<Material> materialList;
    private final OnMaterialClickListener listener;

    public MaterialAdapter(Context context,
                           List<Material> materialList,
                           OnMaterialClickListener listener) {
        this.context = context;
        this.materialList = materialList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_material, parent, false);
        return new MaterialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {

        Material material = materialList.get(position);

        holder.txtName.setText(material.name);
        holder.txtCategory.setText(material.category);
        holder.txtPrice.setText(material.price);
        holder.txtSupplier.setText(material.supplier);
        holder.txtQuantity.setText("Qty: " + material.quantity);

        // 🔥 LOAD IMAGE FROM FIRESTORE URL USING GLIDE
        if (material.imageUrl != null && !material.imageUrl.isEmpty()) {

            Glide.with(context)
                    .load(material.imageUrl)
                    .placeholder(R.drawable.landing_image)  // optional placeholder
                    .error(R.drawable.landing_image)        // fallback image
                    .into(holder.imgMaterial);

        } else {
            holder.imgMaterial.setImageResource(R.drawable.landing_image);
        }

        // 🔥 CLICK LISTENER (UNCHANGED)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMaterialClick(material);
            }
        });
    }

    @Override
    public int getItemCount() {
        return materialList == null ? 0 : materialList.size();
    }

    static class MaterialViewHolder extends RecyclerView.ViewHolder {

        ImageView imgMaterial;
        TextView txtName, txtCategory, txtPrice, txtSupplier, txtQuantity;

        public MaterialViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMaterial = itemView.findViewById(R.id.imgMaterial);
            txtName = itemView.findViewById(R.id.txtMaterialName);
            txtCategory = itemView.findViewById(R.id.txtCategoryName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtSupplier = itemView.findViewById(R.id.txtSupplier);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
        }
    }
}