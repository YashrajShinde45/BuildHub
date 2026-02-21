package com.example.constructiondelivery;

import java.io.Serializable;

public class Material implements Serializable {

    public String id;
    public String name;
    public String category;
    public String price;
    public String quantity;
    public String quantityUnit;
    public String supplier;
    public String shortDesc;
    public String quality;
    public String details;
    public int image;
    public String status;

    // 🔥 NEW FIELDS
    public String supplierId;
    public String productId;
    public String imageUrl;

    public Material() {
    }
}