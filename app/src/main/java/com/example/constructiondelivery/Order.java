package com.example.constructiondelivery;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    public String orderId;
    public String userId;
    public List<Material> items;
    public String totalPrice;
    public String shippingAddress;
    public String orderStatus; // e.g., "placed", "accepted", "rejected", "shipped", "delivered"

    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Order(String orderId, String userId, List<Material> items, String totalPrice, String shippingAddress, String orderStatus) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.shippingAddress = shippingAddress;
        this.orderStatus = orderStatus;
    }
}
