package com.example.constructiondelivery;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    public interface CartLoadListener {
        void onCartLoaded();
    }

    private static final List<CartItem> cartItems = new ArrayList<>();
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static String getUserId() {
        if (mAuth.getCurrentUser() == null) return null;
        return mAuth.getCurrentUser().getUid();
    }

    // 🔥 LOAD CART FROM FIRESTORE
    public static void loadCartFromFirestore(CartLoadListener listener) {

        String userId = getUserId();
        if (userId == null) {
            listener.onCartLoaded();
            return;
        }

        db.collection("users")
                .document(userId)
                .collection("cart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    cartItems.clear();

                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {

                        String materialId =
                                queryDocumentSnapshots.getDocuments().get(i).getString("materialId");

                        long quantity =
                                queryDocumentSnapshots.getDocuments().get(i).getLong("quantity");

                        // 🔥 FETCH MATERIAL FROM FIRESTORE
                        db.collection("materials")
                                .document(materialId)
                                .get()
                                .addOnSuccessListener(doc -> {

                                    if (doc.exists()) {

                                        Material material = new Material();

                                        material.id = doc.getId();
                                        material.name = doc.getString("name");
                                        material.category = doc.getString("category");

                                        Double price = doc.getDouble("price");
                                        material.price = price != null ? "₹" + price : "₹0";

                                        Long qty = doc.getLong("quantity");
                                        material.quantity = qty != null ? String.valueOf(qty) : "0";

                                        material.quantityUnit = doc.getString("quantityUnit");
                                        material.supplier = doc.getString("supplier");
                                        material.shortDesc = doc.getString("shortDesc");
                                        material.quality = doc.getString("quality");
                                        material.details = doc.getString("details");

                                        // ⭐ FIXED: LOAD IMAGE URL FROM CLOUDINARY FIELD
                                        material.imageUrl = doc.getString("imageUrl");

                                        cartItems.add(new CartItem(material, (int) quantity));

                                        listener.onCartLoaded();
                                    }
                                });
                    }
                });
    }

    // ✅ ADD ITEM
    public static void addItem(CartItem item) {

        String userId = getUserId();
        if (userId == null) return;

        String materialId = item.getMaterial().id;

        for (CartItem cartItem : cartItems) {
            if (cartItem.getMaterial().id.equals(materialId)) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                updateQuantityInFirestore(cartItem);
                return;
            }
        }

        cartItems.add(item);

        db.collection("users")
                .document(userId)
                .collection("cart")
                .document(materialId)
                .set(createCartMap(item));
    }

    // ✅ REMOVE ITEM
    public static void removeItem(CartItem item) {

        String userId = getUserId();
        if (userId == null) return;

        cartItems.remove(item);

        db.collection("users")
                .document(userId)
                .collection("cart")
                .document(item.getMaterial().id)
                .delete();
    }

    // ✅ INCREASE QUANTITY
    public static void increaseQuantity(CartItem item) {
        item.setQuantity(item.getQuantity() + 1);
        updateQuantityInFirestore(item);
    }

    // ✅ DECREASE QUANTITY
    public static void decreaseQuantity(CartItem item) {

        String userId = getUserId();
        if (userId == null) return;

        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            updateQuantityInFirestore(item);
        } else {
            removeItem(item);
        }
    }

    // 🔥 UPDATE QUANTITY
    private static void updateQuantityInFirestore(CartItem item) {

        String userId = getUserId();
        if (userId == null) return;

        db.collection("users")
                .document(userId)
                .collection("cart")
                .document(item.getMaterial().id)
                .update("quantity", item.getQuantity());
    }

    // 🔥 CREATE MAP
    private static java.util.Map<String, Object> createCartMap(CartItem item) {

        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("materialId", item.getMaterial().id);
        map.put("quantity", item.getQuantity());
        map.put("addedAt", FieldValue.serverTimestamp());

        return map;
    }

    public static List<CartItem> getCartItems() {
        return cartItems;
    }

    public static void clearCartForCurrentUser() {

        String userId = getUserId();
        if (userId == null) return;

        db.collection("users")
                .document(userId)
                .collection("cart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    queryDocumentSnapshots.getDocuments()
                            .forEach(doc -> doc.getReference().delete());
                });

        cartItems.clear();
    }

    public static void clearLocalCart() {
        cartItems.clear();
    }
}