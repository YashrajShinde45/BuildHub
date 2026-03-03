package com.example.constructiondelivery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
    private boolean isAdmin;

    public OrderAdapter(Context context, List<Order> orderList, boolean isAdmin) {
        this.context = context;
        this.orderList = orderList;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Order order = orderList.get(position);

        holder.orderId.setText("Order ID: " + order.orderId);
        holder.userId.setText("User ID: " + order.userId);
        holder.totalPrice.setText("Total Price: " + order.totalPrice);
        holder.orderStatus.setText(order.orderStatus);

        switch (order.orderStatus) {
            case "Pending":
                holder.orderStatus.setTextColor(Color.GRAY);
                break;
            case "Shipped":
                holder.orderStatus.setTextColor(Color.BLUE);
                break;
            case "Out for Delivery":
                holder.orderStatus.setTextColor(Color.parseColor("#FFA500"));
                break;
            default:
                holder.orderStatus.setTextColor(Color.BLACK);
        }

        if (isAdmin) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("order", order);
                context.startActivity(intent);
            });
        } else {
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