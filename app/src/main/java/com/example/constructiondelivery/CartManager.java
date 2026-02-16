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

    public static void loadCartFromFirestore(CartLoadListener listener) {
        String userId = getUserId();
        if (userId == null) {
            listener.onCartLoaded();
            return;
        }

        db.collection("users").document(userId).collection("cart").get().addOnSuccessListener(queryDocumentSnapshots -> {
            cartItems.clear();
            for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                String materialId = queryDocumentSnapshots.getDocuments().get(i).getString("materialId");
                long quantity = queryDocumentSnapshots.getDocuments().get(i).getLong("quantity");

                Material material = DataHelper.getMaterialById(materialId);
                if (material != null) {
                    cartItems.add(new CartItem(material, (int) quantity));
                }
            }
            listener.onCartLoaded();
        });
    }

    // ✅ ADD ITEM
    public static void addItem(CartItem item) {

        String userId = getUserId();
        if (userId == null) return;

        String materialId = item.getMaterial().id;

        // Check if already exists locally
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
                .document(materialId) // 🔥 materialId as docId
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

    // ✅ UPDATE QUANTITY IN FIRESTORE
    private static void updateQuantityInFirestore(CartItem item) {

        String userId = getUserId();
        if (userId == null) return;

        db.collection("users")
                .document(userId)
                .collection("cart")
                .document(item.getMaterial().id)
                .update("quantity", item.getQuantity());
    }

    // ✅ CREATE MAP
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
