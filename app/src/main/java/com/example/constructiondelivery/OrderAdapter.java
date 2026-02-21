package com.example.constructiondelivery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context context;
    private List<Order> orderList;
    private boolean isAdmin; // 🔥 controls click behavior

    // 🔥 Updated Constructor
    public OrderAdapter(Context context, List<Order> orderList, boolean isAdmin) {
        this.context = context;
        this.orderList = orderList;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Order order = orderList.get(position);

        holder.orderId.setText("Order ID: " + order.orderId);
        holder.userId.setText("User ID: " + order.userId);
        holder.totalPrice.setText("Total Price: " + order.totalPrice);
        holder.orderStatus.setText("Status: " + order.orderStatus);

        // 🔥 Only Admin can click
        if (isAdmin) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("order", order);
                context.startActivity(intent);
            });
        } else {
            // User orders not clickable
            holder.itemView.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, userId, totalPrice, orderStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            userId = itemView.findViewById(R.id.user_id);
            totalPrice = itemView.findViewById(R.id.total_price);
            orderStatus = itemView.findViewById(R.id.order_status);
        }
    }
}
