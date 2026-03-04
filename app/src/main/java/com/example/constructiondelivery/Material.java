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
    public String status;

    // Firebase fields
    public String supplierId;
    public String productId;

    // ⭐ Cloudinary Image URL
    public String imageUrl;

    public Material() {
    }
}