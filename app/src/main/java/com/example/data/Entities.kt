package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val quantity: Int,
    val selectedColor: String,
    val selectedSize: String
)

@Entity(tableName = "wishlist_items")
data class WishlistItem(
    @PrimaryKey val productId: Int,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1, // Single entry
    val name: String,
    val email: String,
    val phone: String,
    val addressLine: String,
    val city: String,
    val pincode: String
)

@Entity(tableName = "customer_orders")
data class CustomerOrder(
    @PrimaryKey val orderId: String,
    val date: Long = System.currentTimeMillis(),
    val totalAmount: Double,
    val status: String, // "Processing", "Shipped", "Delivered"
    val itemCount: Int,
    val summary: String // e.g. "1x Smart Watch 2.0, 1x Obsidian Leather"
)
