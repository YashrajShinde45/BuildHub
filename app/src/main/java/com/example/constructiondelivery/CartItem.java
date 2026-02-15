package com.example.constructiondelivery;

import java.io.Serializable;

public class CartItem implements Serializable {
    private final Material material;
    private int quantity;

    public CartItem(Material material, int quantity) {
        this.material = material;
        this.quantity = quantity;
    }

    public Material getMaterial() {
        return material;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
