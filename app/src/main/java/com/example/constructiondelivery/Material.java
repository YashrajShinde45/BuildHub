package com.example.constructiondelivery;

import java.io.Serializable;

public class Material implements Serializable {
    public String name;
    public String category;
    public String price;
    public String quantity;
    public String quantityUnit; // New field for the unit
    public String supplier;
    public String shortDesc;
    public String quality;
    public String details;
    public int image;
    public String status; // "pending", "accepted", "rejected"

    // Old constructor for existing data
    public Material(String name, String category, String price, String quantity, String supplier, String shortDesc, String quality, String details, int image) {
        this(name, category, price, quantity, "", supplier, shortDesc, quality, details, image, "pending");
    }

    // New, complete constructor
    public Material(String name,
                    String category,
                    String price,
                    String quantity,
                    String quantityUnit,
                    String supplier,
                    String shortDesc,
                    String quality,
                    String details,
                    int image) {
        this(name, category, price, quantity, quantityUnit, supplier, shortDesc, quality, details, image, "pending");
    }

    // Constructor with status
    public Material(String name,
                    String category,
                    String price,
                    String quantity,
                    String quantityUnit,
                    String supplier,
                    String shortDesc,
                    String quality,
                    String details,
                    int image,
                    String status) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.quantityUnit = quantityUnit;
        this.supplier = supplier;
        this.shortDesc = shortDesc;
        this.quality = quality;
        this.details = details;
        this.image = image;
        this.status = status;
    }
}
