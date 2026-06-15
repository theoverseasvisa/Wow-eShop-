package com.example.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DummyProductData
import com.example.data.Product
import com.example.ui.components.*
import com.example.ui.theme.*

// Root routing structure
enum class ScreenRoute {
    HOME,
    SHOP,
    PRODUCT_DETAILS,
    CATEGORIES,
    ABOUT_US,
    CONTACT,
    CART,
    CHECKOUT,
    DASHBOARD
}

// Global visual glass card shared helper
@Composable
fun GlassTitle(title: String, subtitle: String = "") {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (subtitle.isNotEmpty()) {
            Text(
                text = subtitle.uppercase(),
                style = Typography.labelMedium.copy(color = RoyalBlueGlow, letterSpacing = 2.sp)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        Text(
            text = title,
            style = Typography.displayMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(4.dp)
                .clip(CircleShape)
                .background(
                    Brush.horizontalGradient(listOf(RoyalBluePrimary, RoyalBlueGlow, Color.White))
                )
        )
    }
}

// 1. HOME SCREEN
@Composable
fun HomeScreen(
    viewModel: ShopViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var newsletterEmail by remember { mutableStateOf("") }
    var newsletterSubscribed by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(DeepBlackBg)
            .padding(bottom = 80.dp) // Leave safety room for bottom sticky nav bar
    ) {
        // --- PREMIUM 3D HERO SECTION ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(RoyalBlueDeep.copy(alpha = 0.35f), DeepBlackBg)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Background cosmic elements
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(listOf(RoyalBluePrimary.copy(alpha = 0.08f), Color.Transparent)),
                    center = Offset(size.width * 0.2f, size.height * 0.3f),
                    radius = 350f
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Interactive floating 3D smartwatch centerpiece
                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .subtleFloating()
                        .tilt3D()
                        .testTag("hero_3d_centerpiece"),
                    contentAlignment = Alignment.Center
                ) {
                    LuxuryProductCanvas(
                        key = "watch",
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = RoyalBluePrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "INDIA'S ULTIMATE LUXURY HUb",
                    style = Typography.labelMedium.copy(color = RoyalBlueGlow, letterSpacing = 3.sp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Discover Amazing Products\nat Wow eShop",
                    style = Typography.displayLarge.copy(fontSize = 28.sp),
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onNavigate(ScreenRoute.SHOP) },
                    modifier = Modifier
                        .height(50.dp)
                        .widthIn(min = 200.dp)
                        .testTag("hero_shop_now_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary),
                    shape = RoundedCornerShape(25.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 12.dp)
                ) {
                    Text(text = "SHOP NOW", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Shop Now")
                }
            }
        }

        // --- FEATURED CATEGORIES ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            GlassTitle(title = "Luxury Categories", subtitle = "Explore")
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(DummyProductData.categories) { category ->
                    Card(
                        modifier = Modifier
                            .width(110.dp)
                            .clickable {
                                viewModel.setSelectedCategory(category)
                                onNavigate(ScreenRoute.SHOP)
                            }
                            .testTag("cat_card_$category"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal),
                        border = BorderStroke(1.dp, PremiumSilverBorder.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(RoyalBluePrimary.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                val catIcon = when (category) {
                                    "Electronics" -> Icons.Default.Tv
                                    "Fashion" -> Icons.Default.Checkroom
                                    "Home & Kitchen" -> Icons.Default.Kitchen
                                    "Beauty" -> Icons.Default.Face
                                    "Accessories" -> Icons.Default.Watch
                                    "Gadgets" -> Icons.Default.Power
                                    else -> Icons.Default.Dashboard
                                }
                                Icon(catIcon, contentDescription = category, tint = RoyalBlueGlow)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = category,
                                style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.White),
                                maxLines = 1,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // --- TRENDING PRODUCTS SCROLLER ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                GlassTitle(title = "Trending Now", subtitle = "Absolute Hype")
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(viewModel.originalProducts.take(4)) { prod ->
                    ProductGridCard(
                        product = prod,
                        viewModel = viewModel,
                        onDetailClick = {
                            viewModel.selectProduct(prod.id)
                            onNavigate(ScreenRoute.PRODUCT_DETAILS)
                        },
                        modifier = Modifier.width(220.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- BEST SELLERS GRID ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            GlassTitle(title = "Elite Best Sellers", subtitle = "Curated luxury")
            viewModel.originalProducts.takeLast(4).forEach { prod ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .glassmorphic()
                        .clickable {
                            viewModel.selectProduct(prod.id)
                            onNavigate(ScreenRoute.PRODUCT_DETAILS)
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .background(Color(0xFF1E293B), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        LuxuryProductCanvas(key = prod.imageKey, modifier = Modifier.size(56.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = prod.name, style = Typography.titleMedium.copy(color = Color.White), fontWeight = FontWeight.Bold)
                        Text(text = prod.category, style = Typography.bodyMedium.copy(color = DustySlate))
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Rs. ${String.format("%,.2f", prod.price)}", style = Typography.bodyMedium.copy(color = RoyalBlueGlow, fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Rs. ${String.format("%,.2f", prod.originalPrice)}", style = Typography.bodyMedium.copy(color = DustySlate, textDecoration = TextDecoration.LineThrough, fontSize = 11.sp))
                        }
                    }
                    Icon(Icons.Default.ArrowOutward, contentDescription = "Detail", tint = RoyalBlueGlow)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // --- WHY CHOOSE US (TRUST ELEMENTS) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            GlassTitle(title = "Why Elite Shoppers Trust Us", subtitle = "Our Brand Code")
            val trustPoints = listOf(
                Triple(Icons.Filled.VerifiedUser, "Secure Payments", "Fully encrypted 256-bit safe transactions via top gateways like UPI, Card, and Netbanking."),
                Triple(Icons.Filled.Cached, "Easy Returns", "No questions asked. Standard 15-day ultra-fast dynamic premium refund program."),
                Triple(Icons.Filled.LocalShipping, "Escrow Shipping", "Guaranteed fully insured courier dispatch across standard Indian territory."),
                Triple(Icons.Filled.SupportAgent, "24/7 Relationship concierge", "Instant VIP tech assistance desk for real-time tracking.")
            )
            trustPoints.forEach { pt ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .glassmorphic(elevation = 3.dp)
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(RoyalBluePrimary.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(pt.first, contentDescription = pt.second, tint = RoyalBlueGlow, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = pt.second, style = Typography.titleMedium.copy(color = Color.White), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = pt.third, style = Typography.bodyMedium.copy(color = DustySlate))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // --- CUSTOMER REVIEWS SLIDER ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            GlassTitle(title = "Voice of Elite Clients", subtitle = "Incredible support")
            val reviews = listOf(
                Triple("Rohan Dev, Mumbai", "🌟🌟🌟🌟🌟", "The Zenith watch holographic dial looks mesmerizing! It behaves like a sci-fi device in mid-air. Shipping took exactly 2 days to Bandra. Truly premium!"),
                Triple("Pooja Singhal, Delhi", "🌟🌟🌟🌟🌟", "Stunning Obsidian neo-leather coat. The structural asymmetric cut fits elegantly. Sizing assistance via live chat was prompt."),
                Triple("Abhishek Nair, Bangalore", "🌟🌟🌟🌟🌟", "Unbelievable fidelity. Elite sound arc conduction headset is absolute magic. Completely frees up ear holes. 10/10 service.")
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(reviews) { rev ->
                    Card(
                        modifier = Modifier
                            .width(280.dp)
                            .height(180.dp),
                        colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal),
                        border = BorderStroke(1.dp, PremiumSilverBorder.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = rev.first, style = Typography.bodyLarge.copy(color = Color.White, fontWeight = FontWeight.Bold))
                                    Text(text = rev.second, color = MetallicGold)
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(text = rev.third, style = Typography.bodyMedium.copy(color = DustySlate), maxLines = 4)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // --- NEWSLETTER SUBSCRIPTION SECTION ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .glassmorphic(elevation = 16.dp)
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.MailOutline, contentDescription = "Newsletter", tint = RoyalBlueGlow, modifier = Modifier.size(40.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Wow Private Registry",
                    style = Typography.titleLarge.copy(color = Color.White),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Subscribe to receive direct custom releases, elite member promo codes, and luxury restock schedules.",
                    style = Typography.bodyMedium.copy(color = DustySlate),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                if (newsletterSubscribed) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(RoyalBluePrimary.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Registry confirmed! check your inbox.", style = Typography.bodyMedium.copy(color = RoyalBlueGlow, fontWeight = FontWeight.Bold))
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = newsletterEmail,
                            onValueChange = { newsletterEmail = it },
                            placeholder = { Text("Your primary Email...", fontSize = 13.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("newsletter_email_input"),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF1E293B),
                                unfocusedContainerColor = Color(0xFF1E293B),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                if (newsletterEmail.contains("@") && newsletterEmail.contains(".")) {
                                    newsletterSubscribed = true
                                    Toast.makeText(context, "Welcome to the Wow eShop Elite circle!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Please write a valid premium email", Toast.LENGTH_SHORT).show()
                                }
                            })
                        )
                        Box(
                            modifier = Modifier
                                .height(50.dp)
                                .background(RoyalBluePrimary, RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                                .clickable {
                                    if (newsletterEmail.contains("@") && newsletterEmail.contains(".")) {
                                        newsletterSubscribed = true
                                        Toast.makeText(context, "Welcome to the Wow eShop Elite circle!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Please write a valid premium email", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "SUBSCRIBE", style = Typography.labelLarge.copy(color = Color.White))
                        }
                    }
                }
            }
        }
    }
}

// 2. SHOP SCREEN
@Composable
fun ShopScreen(
    viewModel: ShopViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val sortBy by viewModel.sortBy.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()

    var activeQuickViewProduct by remember { mutableStateOf<Product?>(null) }
    var showSortMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlackBg)
            .padding(bottom = 80.dp)
    ) {
        // --- SEARCH HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(RoyalBlueDeep.copy(alpha = 0.15f))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Discover Luxury Storefront",
                    style = Typography.titleLarge.copy(color = Color.White),
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(10.dp))

                // AI-powered style Search UI
                TextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("shop_search_field"),
                    placeholder = { Text("What luxury are you hunting (AI-Enhanced)...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = RoyalBlueGlow) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = DustySlate)
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = LuxuryCharcoal,
                        unfocusedContainerColor = LuxuryCharcoal,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = RoyalBluePrimary,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                )
            }
        }

        // --- FILTER PILLS ---
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                CategoryPill(category = "All", isSelected = selectedCategory == "All") {
                    viewModel.setSelectedCategory("All")
                }
            }
            items(viewModel.categories) { cat ->
                CategoryPill(category = cat, isSelected = selectedCategory == cat) {
                    viewModel.setSelectedCategory(cat)
                }
            }
        }

        // --- RESULTS AND SORT LINE ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${filteredProducts.size} Luxury Assets",
                style = Typography.bodyMedium.copy(color = DustySlate, fontWeight = FontWeight.Bold)
            )

            // Sorting selection dropdown triggering
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(LuxuryCharcoal)
                        .clickable { showSortMenu = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Sort, contentDescription = "Sort", tint = RoyalBlueGlow, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = sortBy, style = Typography.bodyMedium.copy(color = Color.White))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Open", tint = Color.White)
                }

                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false },
                    modifier = Modifier.background(LuxuryCharcoal)
                ) {
                    listOf("Trending", "Price: Low to High", "Price: High to Low", "Top Rated").forEach { opt ->
                        DropdownMenuItem(
                            text = { Text(text = opt, style = Typography.bodyMedium.copy(color = Color.White)) },
                            onClick = {
                                viewModel.setSortBy(opt)
                                showSortMenu = false
                            }
                        )
                    }
                }
            }
        }

        // --- PRODUCT GRID ---
        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.SearchOff, contentDescription = "No products", tint = DustySlate, modifier = Modifier.size(60.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "No Luxury Match Found", style = Typography.titleLarge.copy(color = Color.White))
                    Text(text = "Try clearing queries or category filters.", style = Typography.bodyMedium.copy(color = DustySlate))
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("shop_products_grid"),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredProducts) { product ->
                    ProductGridCard(
                        product = product,
                        viewModel = viewModel,
                        onDetailClick = {
                            viewModel.selectProduct(product.id)
                            onNavigate(ScreenRoute.PRODUCT_DETAILS)
                        },
                        onQuickViewClick = {
                            activeQuickViewProduct = product
                        }
                    )
                }
            }
        }
    }

    // Modal quickview binding
    activeQuickViewProduct?.let { prod ->
        QuickViewProductDialog(
            product = prod,
            viewModel = viewModel,
            onDismiss = { activeQuickViewProduct = null }
        )
    }
}

@Composable
fun CategoryPill(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) RoyalBluePrimary else LuxuryCharcoal)
            .border(width = 1.dp, color = if (isSelected) RoyalBlueGlow else PremiumSilverBorder.copy(alpha = 0.4f), shape = RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("pill_$category")
    ) {
        Text(
            text = category,
            style = Typography.bodyMedium.copy(
                color = if (isSelected) Color.White else SmoothWhite,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

// 3. PRODUCT DETAILS PAGE
@Composable
fun ProductDetailsScreen(
    viewModel: ShopViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    val product by viewModel.selectedProduct.collectAsState()
    val wishlist by viewModel.wishlistItems.collectAsState()
    val context = LocalContext.current

    product?.let { prod ->
        var selectedColor by remember { mutableStateOf(prod.colorHexes.firstOrNull() ?: "#1E56FC") }
        var selectedSize by remember { mutableStateOf(prod.sizes.firstOrNull() ?: "Standard fit") }
        val isWishlisted = wishlist.any { it.productId == prod.id }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DeepBlackBg)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp)
        ) {
            // Header Action Hub
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onNavigate(ScreenRoute.SHOP) },
                    modifier = Modifier.background(LuxuryCharcoal, CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = "SPECIFICATIONS",
                    style = Typography.labelLarge.copy(color = RoyalBlueGlow, letterSpacing = 2.sp)
                )
                IconButton(
                    onClick = { viewModel.toggleWishlist(prod.id) },
                    modifier = Modifier.background(LuxuryCharcoal, CircleShape)
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) ErrorRed else Color.White
                    )
                }
            }

            // Interactive 3D Canvas visual Showcase
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(16.dp)
                    .glassmorphic(elevation = 16.dp)
                    .tilt3D()
                    .testTag("product_details_canvas_box"),
                contentAlignment = Alignment.Center
            ) {
                // Background coordinates matrix
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val steps = 8
                    val linePaint = Color.White.copy(alpha = 0.05f)
                    for (i in 1..steps) {
                        drawLine(linePaint, Offset(size.width * (i.toFloat() / steps), 0f), Offset(size.width * (i.toFloat() / steps), size.height), strokeWidth = 1f)
                        drawLine(linePaint, Offset(0f, size.height * (i.toFloat() / steps)), Offset(size.width, size.height * (i.toFloat() / steps)), strokeWidth = 1f)
                    }
                }

                LuxuryProductCanvas(
                    key = prod.imageKey,
                    modifier = Modifier.size(220.dp),
                    primaryColor = Color(android.graphics.Color.parseColor(selectedColor))
                )
            }

            // Info Box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(RoyalBluePrimary.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = prod.category, style = Typography.labelMedium.copy(color = RoyalBlueGlow, fontWeight = FontWeight.Bold))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Rating", tint = MetallicGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${prod.rating}", style = Typography.bodyLarge.copy(color = Color.White, fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "(${prod.reviewCount} reviews)", style = Typography.bodyMedium.copy(color = DustySlate))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = prod.name,
                    style = Typography.displayMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rs. ${String.format("%,.2f", prod.price)}",
                        style = Typography.titleLarge.copy(color = RoyalBlueGlow, fontSize = 24.sp),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Rs. ${String.format("%,.2f", prod.originalPrice)}",
                        style = Typography.titleMedium.copy(color = DustySlate, textDecoration = TextDecoration.LineThrough),
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Brand Narration
                Text(
                    text = "Description",
                    style = Typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = prod.description,
                    style = Typography.bodyLarge.copy(color = DustySlate, lineHeight = 22.sp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Variational Selections: Size
                Text(
                    text = "Sizing Fit",
                    style = Typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    prod.sizes.forEach { sz ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedSize == sz) RoyalBluePrimary else LuxuryCharcoal)
                                .border(width = 1.dp, color = if (selectedSize == sz) RoyalBlueGlow else Color.Transparent, shape = RoundedCornerShape(10.dp))
                                .clickable { selectedSize = sz }
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text(text = sz, style = Typography.bodyMedium.copy(color = Color.White, fontWeight = FontWeight.Bold))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Specification Matrices Table
                Text(
                    text = "Elite Specifications",
                    style = Typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassmorphic()
                        .padding(8.dp)
                ) {
                    prod.specs.forEachIndexed { i, spec ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (i % 2 == 0) Color(0xFF0F172A).copy(alpha = 0.4f) else Color.Transparent)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = spec.title, style = Typography.bodyMedium.copy(color = DustySlate, fontWeight = FontWeight.Bold))
                            Text(text = spec.value, style = Typography.bodyMedium.copy(color = SmoothWhite))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Primary transaction actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Quick inquiry share button
                    IconButton(
                        onClick = { launchWhatsApp(context, "Inquiry from Wow eShop App: I want to know more about the premium mechanical asset: ${prod.name}") },
                        modifier = Modifier
                            .size(52.dp)
                            .background(Color(0xFF25D366), RoundedCornerShape(12.dp))
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = "Query WhatsApp", tint = Color.White)
                    }

                    Button(
                        onClick = {
                            viewModel.addToCart(prod.id, selectedColor, selectedSize)
                            Toast.makeText(context, "${prod.name} added safely to cart!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("specs_add_to_cart"),
                        colors = ButtonDefaults.buttonColors(containerColor = LuxuryCharcoal),
                        border = BorderStroke(1.dp, PremiumSilverBorder),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = RoyalBlueGlow)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "ADD TO CART", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Button(
                        onClick = {
                            viewModel.addToCart(prod.id, selectedColor, selectedSize)
                            onNavigate(ScreenRoute.CART)
                        },
                        modifier = Modifier
                            .weight(1.1f)
                            .height(52.dp)
                            .testTag("specs_buy_now"),
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "BUY NOW", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Loading luxury details...", color = Color.White)
        }
    }
}

// 4. CATEGORIES SCREEN
@Composable
fun CategoriesScreen(
    viewModel: ShopViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlackBg)
            .padding(bottom = 80.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            GlassTitle(title = "Core Departments", subtitle = "Luxury Catalog")
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(DummyProductData.categories) { category ->
                val quantity = DummyProductData.products.count { it.category.equals(category, ignoreCase = true) }
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .glassmorphic(elevation = 12.dp)
                        .clickable {
                            viewModel.setSelectedCategory(category)
                            onNavigate(ScreenRoute.SHOP)
                        }
                        .testTag("dep_$category"),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Icon Node
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .background(RoyalBluePrimary.copy(alpha = 0.25f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            val catIcon = when (category) {
                                "Electronics" -> Icons.Default.Tv
                                "Fashion" -> Icons.Default.Checkroom
                                "Home & Kitchen" -> Icons.Default.Kitchen
                                "Beauty" -> Icons.Default.Face
                                "Accessories" -> Icons.Default.Watch
                                "Gadgets" -> Icons.Default.Power
                                else -> Icons.Default.Dashboard
                            }
                            Icon(catIcon, contentDescription = category, tint = RoyalBlueGlow)
                        }

                        // Text indicators
                        Column {
                            Text(
                                text = category,
                                style = Typography.titleMedium.copy(color = Color.White),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$quantity Premium Assets",
                                style = Typography.bodyMedium.copy(color = DustySlate, fontSize = 11.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// 5. ABOUT US PAGE
@Composable
fun AboutUsScreen(onNavigate: (ScreenRoute) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlackBg)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 85.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            IconButton(
                onClick = { onNavigate(ScreenRoute.HOME) },
                modifier = Modifier.background(LuxuryCharcoal, CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        GlassTitle(title = "Our Brand Narrative", subtitle = "WOW E-SHOP CRAFT")

        Column(modifier = Modifier.padding(24.dp)) {
            // Elegant illustrative canvas logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .glassmorphic()
                    .subtleFloating()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.LocalActivity, contentDescription = "Wow Badge", tint = RoyalBlueGlow, modifier = Modifier.size(54.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "WOW ESHOP INDIA", style = Typography.titleLarge.copy(color = Color.White, letterSpacing = 2.sp))
                    Text(text = "Est. 2024 • Gurgaon Headquarters", style = Typography.bodyMedium.copy(color = DustySlate))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Our Mission",
                style = Typography.titleLarge.copy(color = Color.White),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "At Wow eShop, we reject the mundane and general defaults of commerce. Our vision is to serve Indian modern collectors with ultra-premium mechanical collectibles, temperature-adaptive wearable clothing, and acoustic bone-conduction arcs that celebrate forward-thinking human engineering.",
                style = Typography.bodyLarge.copy(color = DustySlate, lineHeight = 24.sp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "The Integrity Promise",
                style = Typography.titleLarge.copy(color = Color.White),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Each luxury transaction is hosted within a secure 256-bit encrypted core network. We deploy fully-insured, temperature-moderated premium couriers to move your collectibles from our sterile fulfillment hubs directly to your doors, complete with full authentic warranties.",
                style = Typography.bodyLarge.copy(color = DustySlate, lineHeight = 24.sp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { onNavigate(ScreenRoute.SHOP) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("about_view_catalog"),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "EXPLORE COLLECTION", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 6. CONTACT PAGE
@Composable
fun ContactScreen(onNavigate: (ScreenRoute) -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var inquiryMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlackBg)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 85.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            IconButton(
                onClick = { onNavigate(ScreenRoute.HOME) },
                modifier = Modifier.background(LuxuryCharcoal, CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        GlassTitle(title = "Contact Concierge", subtitle = "24/7 Priority desk")

        Column(modifier = Modifier.padding(20.dp)) {
            // Interactive custom silver/blue vector map showcase
            Text(
                text = "Our Corporate Headquarters",
                style = Typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .glassmorphic()
                    .testTag("google_maps_section"),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    
                    // Draw abstract silver coordinates lines
                    drawRect(color = Color(0xFF0F172A))
                    
                    // Route vectors
                    val rPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(w * 0.1f, h * 0.8f)
                        quadraticTo(w * 0.3f, h * 0.4f, w * 0.5f, h * 0.5f)
                        lineTo(w * 0.8f, h * 0.2f)
                    }
                    drawPath(
                        path = rPath,
                        color = RoyalBlueGlow.copy(alpha = 0.5f),
                        style = Stroke(width = 6f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f)))
                    )

                    // Corporate marker pin
                    drawCircle(color = RoyalBluePrimary, center = Offset(w * 0.5f, h * 0.5f), radius = 20f)
                    drawCircle(color = Color.White, center = Offset(w * 0.5f, h * 0.5f), radius = 8f)
                }
                
                // Overlay Badge
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(12.dp),
                    colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal.copy(alpha = 0.9f))
                ) {
                    Text(
                        text = "Gurgaon CyberCity, Tower 8B, Sector 24",
                        style = Typography.bodyMedium.copy(color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Direct Communication list
            val directDesks = listOf(
                Triple(Icons.Default.Phone, "WhatsApp Support", "+91 98860 08298"),
                Triple(Icons.Default.Email, "Concierge Mailbox", "woweshopindia@gmail.com"),
                Triple(Icons.Default.Language, "Digital Domain", "www.woweshop.in")
            )
            directDesks.forEach { desk ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .glassmorphic(elevation = 2.dp)
                        .clickable {
                            when (desk.first) {
                                Icons.Default.Phone -> launchWhatsApp(context, "Direct inquiry from Wow App contact desk:")
                                Icons.Default.Email -> {
                                    val mailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:${desk.third}")
                                    }
                                    context.startActivity(Intent.createChooser(mailIntent, "Select Concierge Mailer"))
                                }
                                else -> {
                                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://${desk.third}"))
                                    context.startActivity(webIntent)
                                }
                            }
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(RoyalBluePrimary.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(desk.first, contentDescription = desk.second, tint = RoyalBlueGlow, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = desk.second, style = Typography.bodyMedium.copy(color = DustySlate))
                        Text(text = desk.third, style = Typography.bodyMedium.copy(color = Color.White, fontWeight = FontWeight.Bold))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Contact Form
            Text(
                text = "Dispatch Direct Inquiry Ticket",
                style = Typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Your full name...", fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("contact_name"),
                    colors = TextFieldDefaults.colors(focusedContainerColor = LuxuryCharcoal, unfocusedContainerColor = LuxuryCharcoal, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Email identifier...", fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("contact_email"),
                    colors = TextFieldDefaults.colors(focusedContainerColor = LuxuryCharcoal, unfocusedContainerColor = LuxuryCharcoal, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                TextField(
                    value = inquiryMessage,
                    onValueChange = { inquiryMessage = it },
                    placeholder = { Text("Detail your luxury specifications...", fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth().height(100.dp).testTag("contact_message"),
                    colors = TextFieldDefaults.colors(focusedContainerColor = LuxuryCharcoal, unfocusedContainerColor = LuxuryCharcoal, focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                    maxLines = 4
                )

                Button(
                    onClick = {
                        if (name.isNotBlank() && email.contains("@") && inquiryMessage.isNotBlank()) {
                            Toast.makeText(context, "Direct priority ticket dispatched safely!", Toast.LENGTH_SHORT).show()
                            name = ""
                            email = ""
                            inquiryMessage = ""
                        } else {
                            Toast.makeText(context, "Fill out all priority details before dispatching.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("contact_submit"),
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "DISPATCH TICKET", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// 7. CART PAGE
@Composable
fun CartScreen(
    viewModel: ShopViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    val items by viewModel.cartItems.collectAsState()
    val subtotal by viewModel.cartSubtotal.collectAsState()
    val ship by viewModel.shippingFee.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val couponError by viewModel.couponError.collectAsState()
    val discount by viewModel.couponDiscount.collectAsState()
    val total by viewModel.cartTotal.collectAsState()

    var couponInput by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlackBg)
            .padding(bottom = 80.dp)
    ) {
        Box(modifier = Modifier.padding(bottom = 6.dp)) {
            GlassTitle(title = "Your Shopping Chest", subtitle = "Secure Escrow Cart")
        }

        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.ShoppingCartCheckout, contentDescription = "Empty", tint = DustySlate, modifier = Modifier.size(60.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Chest is currently empty", style = Typography.titleLarge.copy(color = Color.White))
                    Text(text = "Add luxury products from our central hubs.", style = Typography.bodyMedium.copy(color = DustySlate))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onNavigate(ScreenRoute.SHOP) },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary)
                    ) {
                        Text(text = "SHOP LUXURY COLLECTION")
                    }
                }
            }
        } else {
            Column(modifier = Modifier.weight(1f)) {
                // Cart Products Stack
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items) { cartItem ->
                        val product = viewModel.originalProducts.firstOrNull { it.id == cartItem.productId }
                        product?.let { prod ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .glassmorphic()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Mini visual
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .background(Color(0xFF1E293B), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    LuxuryProductCanvas(key = prod.imageKey, modifier = Modifier.size(52.dp))
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = prod.name, style = Typography.bodyLarge.copy(color = Color.White, fontWeight = FontWeight.Bold), maxLines = 1)
                                    Text(text = "Size: ${cartItem.selectedSize}", style = Typography.bodyMedium.copy(color = DustySlate))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Rs. ${String.format("%,.0f", prod.price)}",
                                        style = Typography.bodyMedium.copy(color = RoyalBlueGlow, fontWeight = FontWeight.Bold)
                                    )
                                }

                                // Quantity Controls
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { viewModel.updateCartQuantity(cartItem, cartItem.quantity - 1) },
                                        modifier = Modifier.size(28.dp).background(Color(0xFF1E293B), CircleShape)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Minus", tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                    Text(
                                        text = "${cartItem.quantity}",
                                        style = Typography.bodyLarge.copy(color = Color.White, fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 10.dp)
                                    )
                                    IconButton(
                                        onClick = { viewModel.updateCartQuantity(cartItem, cartItem.quantity + 1) },
                                        modifier = Modifier.size(28.dp).background(Color(0xFF1E293B), CircleShape)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                // Billing calculations
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .testTag("checkout_calculators"),
                    colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        // Coupon Code Section
                        Text(text = "Wow Elite Offer Registry", style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.White))
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = couponInput,
                                onValueChange = { couponInput = it },
                                placeholder = { Text("Code: e.g. WOWNEW, LUXURY20...", fontSize = 11.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .testTag("coupon_field"),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF1E293B),
                                    unfocusedContainerColor = Color(0xFF1E293B),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .height(46.dp)
                                    .background(RoyalBluePrimary, RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                                    .clickable {
                                        if (couponInput.isNotBlank()) {
                                            viewModel.applyCouponCode(couponInput)
                                        }
                                    }
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "APPLY", style = Typography.labelMedium.copy(color = Color.White))
                            }
                        }

                        // Coupon validation logs
                        appliedCoupon?.let { cp ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Active Coupon: $cp applied!", color = Color.Green, fontSize = 12.sp)
                                Text(
                                    text = "REMOVE",
                                    color = ErrorRed,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable { viewModel.removeCoupon() }
                                )
                            }
                        }
                        couponError?.let { err ->
                            Text(text = err, color = ErrorRed, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                        }

                        Divider(modifier = Modifier.padding(vertical = 12.dp), color = PremiumSilverBorder.copy(alpha = 0.3f))

                        // Dynamic Bills
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Luxury Subtotal", style = Typography.bodyMedium.copy(color = DustySlate))
                            Text(text = "Rs. ${String.format("%,.2f", subtotal)}", style = Typography.bodyMedium.copy(color = Color.White))
                        }
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Premium Logistics Insured Fee", style = Typography.bodyMedium.copy(color = DustySlate))
                            Text(text = if (ship == 0.0) "FREE" else "Rs. ${String.format("%,.2f", ship)}", style = Typography.bodyMedium.copy(color = if (ship == 0.0) Color.Green else Color.White))
                        }
                        if (discount > 0.0) {
                            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Coupon Reductions", style = Typography.bodyMedium.copy(color = Color.Green))
                                Text(text = "- Rs. ${String.format("%,.2f", discount)}", style = Typography.bodyMedium.copy(color = Color.Green))
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = PremiumSilverBorder.copy(alpha = 0.3f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Secure Total Core Bills", style = Typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold))
                            Text(text = "Rs. ${String.format("%,.2f", total)}", style = Typography.titleLarge.copy(color = RoyalBlueGlow, fontWeight = FontWeight.ExtraBold))
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { onNavigate(ScreenRoute.CHECKOUT) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("cart_proceed_checkout"),
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Checkout")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "PROCEED TO VIP CHECKOUT", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// 8. CHECKOUT PAGE
@Composable
fun CheckoutScreen(
    viewModel: ShopViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    val total by viewModel.cartTotal.collectAsState()
    val currentProfile by viewModel.userProfile.collectAsState()

    var name by remember { mutableStateOf(currentProfile?.name ?: "") }
    var email by remember { mutableStateOf(currentProfile?.email ?: "") }
    var phone by remember { mutableStateOf(currentProfile?.phone ?: "") }
    var address by remember { mutableStateOf(currentProfile?.addressLine ?: "") }
    var city by remember { mutableStateOf(currentProfile?.city ?: "") }
    var pinCode by remember { mutableStateOf(currentProfile?.pincode ?: "") }

    var selectedPayment by remember { mutableStateOf("UPI Link") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlackBg)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            IconButton(
                onClick = { onNavigate(ScreenRoute.CART) },
                modifier = Modifier.background(LuxuryCharcoal, CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        GlassTitle(title = "VIP Secure Escrow", subtitle = "Fulfillment Center")

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            
            // Core address logs
            Card(
                modifier = Modifier.fillMaxWidth().testTag("address_review_registry"),
                colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal),
                border = BorderStroke(1.dp, PremiumSilverBorder.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Shipping Core Coordinates", style = Typography.titleMedium.copy(color = Color.White), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("Fulfillment Recipient Name") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("check_name"),
                        colors = TextFieldDefaults.colors(focusedContainerColor = DeepBlackBg, unfocusedContainerColor = DeepBlackBg, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("Registry primary Email") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("check_email"),
                        colors = TextFieldDefaults.colors(focusedContainerColor = DeepBlackBg, unfocusedContainerColor = DeepBlackBg, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                    TextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text("Delivery Phone, WhatsApp contact") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("check_phone"),
                        colors = TextFieldDefaults.colors(focusedContainerColor = DeepBlackBg, unfocusedContainerColor = DeepBlackBg, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                    TextField(
                        value = address,
                        onValueChange = { address = it },
                        placeholder = { Text("Exact Address, Appartment suite, landmark") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("check_address"),
                        colors = TextFieldDefaults.colors(focusedContainerColor = DeepBlackBg, unfocusedContainerColor = DeepBlackBg, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextField(
                            value = city,
                            onValueChange = { city = it },
                            placeholder = { Text("City, State") },
                            modifier = Modifier.weight(1.1f).padding(vertical = 4.dp).testTag("check_city"),
                            colors = TextFieldDefaults.colors(focusedContainerColor = DeepBlackBg, unfocusedContainerColor = DeepBlackBg, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                        TextField(
                            value = pinCode,
                            onValueChange = { pinCode = it },
                            placeholder = { Text("ZIP") },
                            modifier = Modifier.weight(0.9f).padding(vertical = 4.dp).testTag("check_pincode"),
                            colors = TextFieldDefaults.colors(focusedContainerColor = DeepBlackBg, unfocusedContainerColor = DeepBlackBg, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                    }
                }
            }

            // Pay channel choices
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal),
                border = BorderStroke(1.dp, PremiumSilverBorder.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Indian Decimated Pay Gateways", style = Typography.titleMedium.copy(color = Color.White), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    val gates = listOf("UPI Link (Fully Automated)", "Secure CC/DC RazorPay", "Cash On VIP Delivery")
                    gates.forEach { gateway ->
                        val active = selectedPayment == gateway
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (active) RoyalBluePrimary.copy(alpha = 0.2f) else DeepBlackBg)
                                .clickable { selectedPayment = gateway }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (active) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                    contentDescription = "Sel",
                                    tint = if (active) RoyalBlueGlow else DustySlate
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = gateway, style = Typography.bodyLarge.copy(color = Color.White, fontWeight = FontWeight.Bold))
                            }
                            Icon(Icons.Default.Security, contentDescription = "Safe", tint = RoyalBlueGlow.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            // Summary box
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Insured VIP CORE Total", style = Typography.bodyLarge.copy(color = Color.White, fontWeight = FontWeight.Bold))
                        Text(text = "Rs. ${String.format("%,.2f", total)}", style = Typography.titleLarge.copy(color = RoyalBlueGlow, fontWeight = FontWeight.ExtraBold))
                    }
                }
            }

            // Checkout CTA
            Button(
                onClick = {
                    if (name.isNotBlank() && email.contains("@") && phone.isNotBlank() && address.isNotBlank() && city.isNotBlank() && pinCode.isNotBlank()) {
                        val orderId = viewModel.checkout(name, email, phone, address, city, pinCode)
                        Toast.makeText(context, "Order $orderId placed successfully!", Toast.LENGTH_LONG).show()
                        onNavigate(ScreenRoute.DASHBOARD)
                    } else {
                        Toast.makeText(context, "Fill physical delivery registry completely.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_checkout_action"),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.VerifiedUser, contentDescription = "Lock")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "EXECUTE SECURE BANK TRANSACTION", fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            }
        }
    }
}

// 9. CLIENT DASHBOARD PAGE
@Composable
fun DashboardScreen(
    viewModel: ShopViewModel,
    onNavigate: (ScreenRoute) -> Unit
) {
    val profile by viewModel.userProfile.collectAsState()
    val wishlist by viewModel.wishlistItems.collectAsState()
    val orders by viewModel.ordersHistory.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlackBg)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 85.dp)
    ) {
        Box(modifier = Modifier.padding(bottom = 6.dp)) {
            GlassTitle(title = "Client Dashboard", subtitle = "VIP Registry Hub")
        }

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            
            // Profile Card
            profile?.let { prof ->
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("profile_section"),
                    colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal),
                    border = BorderStroke(1.dp, PremiumSilverBorder.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .background(RoyalBluePrimary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = prof.name.take(1), style = Typography.titleMedium.copy(color = Color.White))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = prof.name, style = Typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold))
                                    Text(text = prof.email, style = Typography.bodyMedium.copy(color = DustySlate))
                                }
                            }
                            IconButton(
                                onClick = { launchWhatsApp(context, "Direct priority assistance for VIP user: ${prof.name}") },
                                modifier = Modifier.background(DeepBlackBg, CircleShape)
                            ) {
                                Icon(Icons.Default.SupportAgent, contentDescription = "Help Desk", tint = RoyalBlueGlow)
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 14.dp), color = PremiumSilverBorder.copy(alpha = 0.2f))

                        Text(text = "Escrow Delivery coordinates", style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = RoyalBlueGlow))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = prof.addressLine, style = Typography.bodyMedium.copy(color = SmoothWhite))
                        Text(text = "${prof.city} - ${prof.pincode}", style = Typography.bodyMedium.copy(color = DustySlate))
                        Text(text = "Phone: ${prof.phone}", style = Typography.bodyMedium.copy(color = DustySlate))
                    }
                }
            }

            // Wishlist
            Card(
                modifier = Modifier.fillMaxWidth().testTag("wish_section"),
                colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal),
                border = BorderStroke(1.dp, PremiumSilverBorder.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Your Luxury Wishlist", style = Typography.titleMedium.copy(color = Color.White), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    if (wishlist.isEmpty()) {
                        Text(text = "No wish items recorded currently.", style = Typography.bodyMedium.copy(color = DustySlate))
                    } else {
                        wishlist.forEach { wish ->
                            val prod = viewModel.originalProducts.firstOrNull { it.id == wish.productId }
                            prod?.let { p ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .background(DeepBlackBg, RoundedCornerShape(10.dp))
                                        .clickable {
                                            viewModel.selectProduct(p.id)
                                            onNavigate(ScreenRoute.PRODUCT_DETAILS)
                                        }
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(46.dp)
                                                .background(Color(0xFF1E293B), RoundedCornerShape(8.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            LuxuryProductCanvas(key = p.imageKey, modifier = Modifier.size(36.dp))
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(text = p.name, style = Typography.bodyMedium.copy(color = Color.White, fontWeight = FontWeight.Bold))
                                            Text(text = "Rs. ${String.format("%,.0f", p.price)}", style = Typography.bodyMedium.copy(color = RoyalBlueGlow))
                                        }
                                    }
                                    IconButton(onClick = { viewModel.toggleWishlist(p.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Remove", tint = ErrorRed)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Orders history log
            Card(
                modifier = Modifier.fillMaxWidth().testTag("orders_history_registry"),
                colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal),
                border = BorderStroke(1.dp, PremiumSilverBorder.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "VIP Fulfillment History", style = Typography.titleMedium.copy(color = Color.White), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    if (orders.isEmpty()) {
                        Text(text = "No historic transactions logged yet.", style = Typography.bodyMedium.copy(color = DustySlate))
                    } else {
                        orders.forEach { ord ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(DeepBlackBg, RoundedCornerShape(12.dp))
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = ord.orderId, style = Typography.bodyMedium.copy(color = RoyalBlueGlow, fontWeight = FontWeight.Bold))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(RoyalBluePrimary.copy(alpha = 0.15f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(text = ord.status, style = Typography.bodyMedium.copy(color = RoyalBlueGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold))
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "Assets: ${ord.summary}", style = Typography.bodyMedium.copy(color = SmoothWhite))
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Total Insured Bills", style = Typography.bodyMedium.copy(color = DustySlate))
                                    Text(text = "Rs. ${String.format("%,.2f", ord.totalAmount)}", style = Typography.bodyMedium.copy(color = Color.White, fontWeight = FontWeight.Bold))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Global Reusable Product Grid Card supporting 3D Tilt
@Composable
fun ProductGridCard(
    product: Product,
    viewModel: ShopViewModel,
    onDetailClick: () -> Unit,
    onQuickViewClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val wishlist by viewModel.wishlistItems.collectAsState()
    val isWishlisted = wishlist.any { it.productId == product.id }
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .tilt3D() // 3D Interactive skew on touch drag!
            .clickable { onDetailClick() }
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal),
        border = BorderStroke(1.dp, PremiumSilverBorder.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillModifierHeightForCard()
                    .background(Color(0xFF0F172A))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                // Top Action overlays
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rating tag
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(DeepBlackBg.copy(alpha = 0.7f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = "Star", tint = MetallicGold, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = "${product.rating}", fontSize = 11.sp, color = Color.White)
                        }
                    }
                    
                    // Favorite toggle node
                    IconButton(
                        onClick = { viewModel.toggleWishlist(product.id) },
                        modifier = Modifier
                            .size(28.dp)
                            .background(DeepBlackBg.copy(alpha = 0.7f), CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Save",
                            tint = if (isWishlisted) ErrorRed else Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }

                // Main 3D graphic core
                LuxuryProductCanvas(
                    key = product.imageKey,
                    modifier = Modifier.size(110.dp)
                )

                // Quick View node overlay
                onQuickViewClick?.let { qv ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp)
                    ) {
                        IconButton(
                            onClick = qv,
                            modifier = Modifier
                                .size(28.dp)
                                .background(RoyalBluePrimary, CircleShape)
                        ) {
                            Icon(Icons.Default.Visibility, contentDescription = "Quick view", tint = Color.White, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }

            // Descriptions Node
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.name,
                    style = Typography.bodyLarge.copy(color = Color.White, fontWeight = FontWeight.Bold),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = product.category, style = Typography.bodyMedium.copy(color = DustySlate))
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Rs. ${String.format("%,.0f", product.price)}",
                            style = Typography.bodyLarge.copy(color = RoyalBlueGlow, fontWeight = FontWeight.Bold)
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.addToCart(product.id, product.colorHexes.firstOrNull() ?: "#1E56FC", product.sizes.firstOrNull() ?: "Standard")
                            Toast.makeText(context, "${product.name} safe in cart!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .background(RoyalBluePrimary, RoundedCornerShape(10.dp))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Fast", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

// Clean helper to manage card vertical bounds
@Composable
fun Modifier.fillModifierHeightForCard(): Modifier = this.height(130.dp).fillMaxWidth()
