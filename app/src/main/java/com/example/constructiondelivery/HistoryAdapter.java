package com.example.constructiondelivery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;

    public HistoryAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productStatus.setText(product.getStatus());

        if (product.getStatus().equalsIgnoreCase("pending")) {
            holder.productStatus.setBackgroundColor(Color.parseColor("#FF9800")); // Orange
        } else {
            holder.productStatus.setBackgroundColor(Color.parseColor("#4CAF50")); // Green
        }

        holder.itemView.setOnClickListener(v -> {
            if (product.getStatus().equalsIgnoreCase("pending")) {
                new AlertDialog.Builder(context)
                        .setTitle("Approve Product")
                        .setMessage("Do you want to approve this product?")
                        .setPositiveButton("Approve", (dialog, which) -> {
                            product.setStatus("Approved");
                            notifyItemChanged(position);
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tvHistoryProductName);
            productStatus = itemView.findViewById(R.id.tvHistoryProductStatus);
        }
    }
}
