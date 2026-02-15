package com.example.constructiondelivery;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final List<CartItem> cartItems = new ArrayList<>();

    public static void addItem(CartItem item) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getMaterial().name.equals(item.getMaterial().name)) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        cartItems.add(item);
    }

    public static void removeItem(CartItem item) {
        cartItems.remove(item);
    }

    public static void increaseQuantity(CartItem item) {
        item.setQuantity(item.getQuantity() + 1);
    }

    public static void decreaseQuantity(CartItem item) {
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
        } else {
            removeItem(item);
        }
    }

    public static List<CartItem> getCartItems() {
        return cartItems;
    }

    public static void clearCart() {
        cartItems.clear();
    }
}
