package com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

/**
 * Centralized cart manager for sale items.
 * This serves as the single source of truth for cart items across the application.
 */
object SaleCartDataHolder {
    private val cartItems = mutableStateListOf<SeedBatchStockOutData>()
    private var nextId = 1

    /**
     * Get the cart items as a SnapshotStateList for reactive UI updates
     */
    fun getCartItems(): SnapshotStateList<SeedBatchStockOutData> {
        return cartItems
    }

    /**
     * Add a single item to the cart
     */
    fun addItem(item: SeedBatchStockOutData) {
        cartItems.add(item.copy(id = nextId++))
    }

    /**
     * Add multiple items to the cart
     */
    fun addItems(items: List<SeedBatchStockOutData>) {
        items.forEach { item ->
            cartItems.add(item.copy(id = nextId++))
        }
    }

    /**
     * Remove an item from the cart
     */
    fun removeItem(item: SeedBatchStockOutData) {
        cartItems.remove(item)
    }

    /**
     * Update an item in the cart
     */
    fun updateItem(oldItem: SeedBatchStockOutData, newItem: SeedBatchStockOutData) {
        val index = cartItems.indexOf(oldItem)
        if (index != -1) {
            cartItems[index] = newItem
        }
    }

    /**
     * Clear all items from the cart
     */
    fun clearCart() {
        cartItems.clear()
    }

    /**
     * Get the number of items in the cart
     */
    fun getCartSize(): Int {
        return cartItems.size
    }

    /**
     * Check if the cart is empty
     */
    fun isEmpty(): Boolean {
        return cartItems.isEmpty()
    }

    // Legacy methods for backward compatibility during transition
    @Deprecated("Use getCartItems() instead", ReplaceWith("getCartItems()"))
    fun getPendingItems(): List<SeedBatchStockOutData>? {
        return if (cartItems.isNotEmpty()) cartItems.toList() else null
    }

    @Deprecated("Use addItems() instead", ReplaceWith("addItems(items)"))
    fun setPendingItems(items: List<SeedBatchStockOutData>) {
        clearCart()
        addItems(items)
    }

    @Deprecated("Use clearCart() instead", ReplaceWith("clearCart()"))
    fun clearPendingItems() {
        clearCart()
    }
}