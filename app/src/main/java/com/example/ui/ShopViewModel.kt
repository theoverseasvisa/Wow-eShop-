package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ShopViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = ShopRepository(db)

    // Raw static catalog data
    val originalProducts = DummyProductData.products
    val categories = DummyProductData.categories

    // Search and Filter UI State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _sortBy = MutableStateFlow("Trending") // "Trending", "Price: Low to High", "Price: High to Low", "Top Rated"
    val sortBy = _sortBy.asStateFlow()

    // Screen-level selection state (Quick View, Product Detail)
    private val _selectedProductId = MutableStateFlow<Int?>(null)
    val selectedProductId = _selectedProductId.asStateFlow()

    val selectedProduct: StateFlow<Product?> = _selectedProductId
        .map { id -> originalProducts.firstOrNull { it.id == id } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // DB-backed Reactive Flows
    val cartItems = repository.cartItems.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val wishlistItems = repository.wishlistItems.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val userProfile = repository.userProfile.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val ordersHistory = repository.orders.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Coupon Code Management
    private val _appliedCoupon = MutableStateFlow<String?>(null)
    val appliedCoupon = _appliedCoupon.asStateFlow()

    private val _couponError = MutableStateFlow<String?>(null)
    val couponError = _couponError.asStateFlow()

    // Calculated Product stream based on Query + Category + Sort
    val filteredProducts: StateFlow<List<Product>> = combine(
        _searchQuery,
        _selectedCategory,
        _sortBy
    ) { query, category, sort ->
        var list = originalProducts

        // Category Filter
        if (category != "All") {
            list = list.filter { it.category.equals(category, ignoreCase = true) }
        }

        // Search Query Filter
        if (query.isNotEmpty()) {
            list = list.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
            }
        }

        // Sorting Logic
        when (sort) {
            "Price: Low to High" -> list.sortedBy { it.price }
            "Price: High to Low" -> list.sortedByDescending { it.price }
            "Top Rated" -> list.sortedByDescending { it.rating }
            else -> list // "Trending" / Standard Order
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), originalProducts)

    // Shopping Cart Calculations
    val cartSubtotal: StateFlow<Double> = cartItems.map { list ->
        list.sumOf { cartItem ->
            val product = originalProducts.firstOrNull { it.id == cartItem.productId }
            (product?.price ?: 0.0) * cartItem.quantity
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val shippingFee: StateFlow<Double> = cartSubtotal.map { subtotal ->
        if (subtotal == 0.0 || subtotal >= 15000.0) 0.0 else 499.0 // Free shipping above Rs 15k!
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val couponDiscount: StateFlow<Double> = combine(cartSubtotal, _appliedCoupon) { subtotal, coupon ->
        if (coupon == null || subtotal == 0.0) return@combine 0.0
        when (coupon.uppercase()) {
            "WOWNEW" -> 2000.0 // Flat 2000 Rs off
            "LUXURY20" -> subtotal * 0.20 // 20% off
            "ELITE" -> if (subtotal >= 20000.0) 5000.0 else 0.0 // Flat 5000 Rs off above 20k
            else -> 0.0
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cartTotal: StateFlow<Double> = combine(cartSubtotal, shippingFee, couponDiscount) { subtotal, ship, discount ->
        val temp = subtotal + ship - discount
        if (temp < 0) 0.0 else temp
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Actions
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSortBy(sort: String) {
        _sortBy.value = sort
    }

    fun selectProduct(productId: Int?) {
        _selectedProductId.value = productId
    }

    fun addToCart(productId: Int, color: String, size: String) {
        viewModelScope.launch {
            repository.addToCart(productId, color, size)
        }
    }

    fun updateCartQuantity(item: CartItem, newQty: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(item, newQty)
        }
    }

    fun removeFromCart(item: CartItem) {
        viewModelScope.launch {
            repository.removeFromCart(item)
        }
    }

    fun toggleWishlist(productId: Int) {
        viewModelScope.launch {
            repository.toggleWishlist(productId, wishlistItems.value)
        }
    }

    fun applyCouponCode(code: String): Boolean {
        _couponError.value = null
        val sub = cartSubtotal.value
        if (sub == 0.0) {
            _couponError.value = "Your Cart is empty"
            return false
        }
        val cleanCode = code.trim().uppercase()
        when (cleanCode) {
            "WOWNEW" -> {
                _appliedCoupon.value = cleanCode
                return true
            }
            "LUXURY20" -> {
                _appliedCoupon.value = cleanCode
                return true
            }
            "ELITE" -> {
                if (sub >= 20000.0) {
                    _appliedCoupon.value = cleanCode
                    return true
                } else {
                    _couponError.value = "ELITE needs cart value >= Rs 20,000"
                    return false
                }
            }
            else -> {
                _couponError.value = "Invalid premium coupon code"
                return false
            }
        }
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
        _couponError.value = null
    }

    fun saveProfile(name: String, email: String, phone: String, address: String, city: String, pin: String) {
        viewModelScope.launch {
            val prof = UserProfile(id = 1, name = name, email = email, phone = phone, addressLine = address, city = city, pincode = pin)
            repository.saveUserProfile(prof)
        }
    }

    fun checkout(name: String, email: String, phone: String, address: String, city: String, pin: String): String {
        val total = cartTotal.value
        val itemsList = cartItems.value
        if (itemsList.isEmpty()) return ""

        val orderId = "WOW-" + (1000..9999).random() + "-" + ('A'..'Z').random() + ('A'..'Z').random()
        val summaryItems = itemsList.map { ci ->
            val prod = originalProducts.firstOrNull { it.id == ci.productId }
            "${ci.quantity}x ${prod?.name ?: "Unknown"}"
        }.joinToString(", ")

        viewModelScope.launch {
            // Save Profile data
            val prof = UserProfile(id = 1, name = name, email = email, phone = phone, addressLine = address, city = city, pincode = pin)
            repository.saveUserProfile(prof)

            // Submit order
            val newOrder = CustomerOrder(
                orderId = orderId,
                totalAmount = total,
                status = "Processing",
                itemCount = itemsList.sumOf { it.quantity },
                summary = summaryItems
            )
            repository.createOrder(newOrder)

            // Empty shopping cart
            repository.clearCart()
            removeCoupon()
        }
        return orderId
    }

    init {
        // Pre-populate profile with standard premium mock placeholder if empty, but let user overwrite
        viewModelScope.launch {
            userProfile.first()?.let { /* Already populated */ } ?: run {
                repository.saveUserProfile(
                    UserProfile(
                        id = 1,
                        name = "Rajesh Sharma",
                        email = "rajesh.sharma@luxury.in",
                        phone = "+91 98960 08298",
                        addressLine = "Flat 402, Royal Palms, Sector 54",
                        city = "Gurgaon, Haryana",
                        pincode = "122011"
                    )
                )
            }
        }
    }
}
