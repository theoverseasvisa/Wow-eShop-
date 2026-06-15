package com.example.data

import kotlinx.coroutines.flow.Flow

class ShopRepository(private val db: AppDatabase) {

    val cartItems: Flow<List<CartItem>> = db.cartDao().getAllCartItems()
    val wishlistItems: Flow<List<WishlistItem>> = db.wishlistDao().getAllWishlistItems()
    val userProfile: Flow<UserProfile?> = db.profileDao().getProfile()
    val orders: Flow<List<CustomerOrder>> = db.orderDao().getAllOrders()

    suspend fun addToCart(productId: Int, color: String, size: String) {
        val newItem = CartItem(
            productId = productId,
            quantity = 1,
            selectedColor = color,
            selectedSize = size
        )
        db.cartDao().insertCartItem(newItem)
    }

    suspend fun updateCartQuantity(item: CartItem, newQty: Int) {
        if (newQty <= 0) {
            db.cartDao().deleteCartItem(item)
        } else {
            db.cartDao().insertCartItem(item.copy(quantity = newQty))
        }
    }

    suspend fun removeFromCart(item: CartItem) {
        db.cartDao().deleteCartItem(item)
    }

    suspend fun clearCart() {
        db.cartDao().clearCart()
    }

    suspend fun toggleWishlist(productId: Int, currentList: List<WishlistItem>) {
        val matches = currentList.any { it.productId == productId }
        if (matches) {
            db.wishlistDao().deleteWishlistById(productId)
        } else {
            db.wishlistDao().insertWishlist(WishlistItem(productId = productId))
        }
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        db.profileDao().insertProfile(profile)
    }

    suspend fun createOrder(order: CustomerOrder) {
        db.orderDao().insertOrder(order)
    }
}
