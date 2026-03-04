package com.example.constructiondelivery;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataHelper {

    private static List<Material> allMaterials;

    private static String userAddress =
            "A-123, Rosewood Apartments, MG Road, Pune, Maharashtra - 411001";

    public static String getUserAddress() {
        return userAddress;
    }

    public static void setUserAddress(String address) {
        userAddress = address;
    }

    public static List<Material> getAllMaterials() {

        if (allMaterials == null) {

            allMaterials = new ArrayList<>();

            // ⭐ Default image placeholder
            String img = String.valueOf(R.drawable.landing_image);

            addMaterial("River Sand", "Natural Materials", "₹1,200/ton",
                    "1 Ton", "", "Local Quarry",
                    "Fine sand for plastering",
                    "Grade-A, Triple washed",
                    "Sourced from local river beds.",
                    img);

            addMaterial("OPC 53 Grade", "Cement & Binding Materials",
                    "₹380/bag", "50 Kg", "", "UltraTech",
                    "High strength concrete",
                    "IS 12269",
                    "Best for RCC structures.",
                    img);

            addMaterial("Fly Ash Bricks", "Bricks & Blocks",
                    "₹7/piece", "1 Piece", "", "Green Build",
                    "Masonry walls",
                    "Class 7.5",
                    "Eco-friendly bricks.",
                    img);
        }

        return allMaterials;
    }

    private static void addMaterial(String name,
                                    String category,
                                    String price,
                                    String quantity,
                                    String quantityUnit,
                                    String supplier,
                                    String shortDesc,
                                    String quality,
                                    String details,
                                    String imageUrl) {

        Material material = new Material();

        material.id = UUID.randomUUID().toString();
        material.name = name;
        material.category = category;
        material.price = price;
        material.quantity = quantity;
        material.quantityUnit = quantityUnit;
        material.supplier = supplier;
        material.shortDesc = shortDesc;
        material.quality = quality;
        material.details = details;

        // ⭐ Image field used everywhere in app
        material.imageUrl = imageUrl;

        material.status = "Pending";

        allMaterials.add(material);
    }

    public static Material getMaterialById(String id) {

        for (Material material : getAllMaterials()) {

            if (material.id != null && material.id.equals(id)) {
                return material;
            }

            // ⭐ also check productId (Firestore products)
            if (material.productId != null && material.productId.equals(id)) {
                return material;
            }
        }

        return null;
    }
}