package com.example.constructiondelivery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnCartItemChangedListener {
        void onItemRemoved(CartItem item);
        void onQuantityChanged();
    }

    private final List<CartItem> cartItems;
    private final OnCartItemChangedListener listener;
    private final Context context;

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartItemChangedListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        Material material = cartItem.getMaterial();

        holder.itemName.setText(material.name);
        holder.itemSupplier.setText("By: " + material.supplier);
        holder.itemPrice.setText(material.price);
        holder.itemQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.itemImage.setImageResource(material.image);

        // --- CLICK LISTENERS ---
        holder.btnRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemRemoved(cartItem);
            }
        });

        holder.btnIncrease.setOnClickListener(v -> {
            CartManager.increaseQuantity(cartItem);
            if (listener != null) {
                listener.onQuantityChanged();
            }
        });

        holder.btnDecrease.setOnClickListener(v -> {
            CartManager.decreaseQuantity(cartItem);
            if (listener != null) {
                listener.onQuantityChanged();
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("material", material);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemSupplier, itemPrice, itemQuantity;
        Button btnRemove, btnIncrease, btnDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.imgCartItem);
            itemName = itemView.findViewById(R.id.txtCartItemName);
            itemSupplier = itemView.findViewById(R.id.txtCartItemSupplier);
            itemPrice = itemView.findViewById(R.id.txtCartItemPrice);
            itemQuantity = itemView.findViewById(R.id.txtCartItemQuantity);
            btnRemove = itemView.findViewById(R.id.btnRemoveFromCart);
            btnIncrease = itemView.findViewById(R.id.btnIncreaseQuantity);
            btnDecrease = itemView.findViewById(R.id.btnDecreaseQuantity);
        }
    }
}
