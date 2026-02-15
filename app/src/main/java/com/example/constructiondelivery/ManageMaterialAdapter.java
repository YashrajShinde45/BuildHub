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

import java.util.List;

public class ManageMaterialAdapter extends RecyclerView.Adapter<ManageMaterialAdapter.ViewHolder> {

    private Context context;
    private List<Material> materialList;

    public ManageMaterialAdapter(Context context, List<Material> materialList) {
        this.context = context;
        this.materialList = materialList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manage_material, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Material material = materialList.get(position);
        holder.materialName.setText(material.name);
        holder.materialCategory.setText(material.category);
        holder.materialPrice.setText(material.price);

        holder.acceptButton.setOnClickListener(v -> {
            // Handle accept
            Toast.makeText(context, "Accepted: " + material.name, Toast.LENGTH_SHORT).show();
        });

        holder.rejectButton.setOnClickListener(v -> {
            // Handle reject
            materialList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, materialList.size());
            Toast.makeText(context, "Rejected: " + material.name, Toast.LENGTH_SHORT).show();
        });
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
