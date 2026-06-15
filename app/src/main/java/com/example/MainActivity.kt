package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.ui.*
import com.example.ui.components.LiveChatWidget
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable Edge-to-Edge full screen rendering
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val shopViewModel: ShopViewModel = viewModel()
                
                // Track active navigation routes
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: ScreenRoute.HOME.name

                // Observe active items count in real-time to overlay a badge on bottom navigation
                val cartItems by shopViewModel.cartItems.collectAsState()
                val cartBadgeCount = cartItems.sumOf { it.quantity }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        // High-contrast, sleek top bar
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("top_branding_header"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal),
                            border = BorderStroke(1.dp, PremiumSilverBorder.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocalActivity,
                                        contentDescription = "Wow Logo",
                                        tint = RoyalBlueGlow,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Wow eShop",
                                        style = Typography.titleMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.ExtraBold,
                                            letterSpacing = 1.sp
                                        )
                                    )
                                }

                                // Quick brand shortcuts (About Us, Contact Desk)
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    IconButton(
                                        onClick = { navController.navigate(ScreenRoute.ABOUT_US.name) },
                                        modifier = Modifier.size(34.dp).background(DeepBlackBg, CircleShape)
                                    ) {
                                        Icon(Icons.Default.Info, contentDescription = "About Us", tint = HighContrastSilver, modifier = Modifier.size(16.dp))
                                    }
                                    IconButton(
                                        onClick = { navController.navigate(ScreenRoute.CONTACT.name) },
                                        modifier = Modifier.size(34.dp).background(DeepBlackBg, CircleShape)
                                    ) {
                                        Icon(Icons.Default.ContactSupport, contentDescription = "Contact Concierge", tint = HighContrastSilver, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    },
                    bottomBar = {
                        // Floating sticky bottom navigation bar with Glassmorphism
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                .testTag("sticky_bottom_nav"),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal.copy(alpha = 0.95f)),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
                        ) {
                            NavigationBar(
                                containerColor = Color.Transparent,
                                modifier = Modifier.height(72.dp)
                            ) {
                                val navItems = listOf(
                                    Triple(ScreenRoute.HOME, Icons.Default.Home, "Home"),
                                    Triple(ScreenRoute.SHOP, Icons.Default.Search, "Discover"),
                                    Triple(ScreenRoute.CATEGORIES, Icons.Default.GridView, "Categories"),
                                    Triple(ScreenRoute.CART, Icons.Default.ShoppingCart, "Cart"),
                                    Triple(ScreenRoute.DASHBOARD, Icons.Default.Person, "Dashboard")
                                )

                                navItems.forEach { item ->
                                    val routeKey = item.first.name
                                    val selected = currentRoute == routeKey

                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = {
                                            if (currentRoute != routeKey) {
                                                navController.navigate(routeKey) {
                                                    popUpTo(ScreenRoute.HOME.name) { saveState = true }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        },
                                        icon = {
                                            Box(contentAlignment = Alignment.TopEnd) {
                                                Icon(
                                                    imageVector = item.second,
                                                    contentDescription = item.third,
                                                    tint = if (selected) RoyalBlueGlow else DustySlate
                                                )
                                                // Dynamic shopping cart item numbers badge
                                                if (item.first == ScreenRoute.CART && cartBadgeCount > 0) {
                                                    Box(
                                                        modifier = Modifier
                                                            .offset(x = 10.dp, y = (-8).dp)
                                                            .background(ErrorRed, CircleShape)
                                                            .size(17.dp),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            text = "$cartBadgeCount",
                                                            fontSize = 10.sp,
                                                            color = Color.White,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            }
                                        },
                                        label = {
                                            Text(
                                                text = item.third,
                                                fontSize = 11.sp,
                                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (selected) Color.White else DustySlate
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = RoyalBluePrimary.copy(alpha = 0.25f)
                                        )
                                    )
                                }
                            }
                        }
                    },
                    containerColor = DeepBlackBg
                ) { innerPadding ->
                    // Root NavHost overlaying screens
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = innerPadding.calculateTopPadding())
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = ScreenRoute.HOME.name
                        ) {
                            composable(ScreenRoute.HOME.name) {
                                HomeScreen(viewModel = shopViewModel, onNavigate = { route ->
                                    navController.navigate(route.name)
                                })
                            }
                            composable(ScreenRoute.SHOP.name) {
                                ShopScreen(viewModel = shopViewModel, onNavigate = { route ->
                                    navController.navigate(route.name)
                                })
                            }
                            composable(ScreenRoute.PRODUCT_DETAILS.name) {
                                ProductDetailsScreen(viewModel = shopViewModel, onNavigate = { route ->
                                    navController.navigate(route.name)
                                })
                            }
                            composable(ScreenRoute.CATEGORIES.name) {
                                CategoriesScreen(viewModel = shopViewModel, onNavigate = { route ->
                                    navController.navigate(route.name)
                                })
                            }
                            composable(ScreenRoute.ABOUT_US.name) {
                                AboutUsScreen(onNavigate = { route ->
                                    navController.navigate(route.name)
                                })
                            }
                            composable(ScreenRoute.CONTACT.name) {
                                ContactScreen(onNavigate = { route ->
                                    navController.navigate(route.name)
                                })
                            }
                            composable(ScreenRoute.CART.name) {
                                CartScreen(viewModel = shopViewModel, onNavigate = { route ->
                                    navController.navigate(route.name)
                                })
                            }
                            composable(ScreenRoute.CHECKOUT.name) {
                                CheckoutScreen(viewModel = shopViewModel, onNavigate = { route ->
                                    navController.navigate(route.name)
                                })
                            }
                            composable(ScreenRoute.DASHBOARD.name) {
                                DashboardScreen(viewModel = shopViewModel, onNavigate = { route ->
                                    navController.navigate(route.name)
                                })
                            }
                        }

                        // Omnipresent floating luxury support desk
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = innerPadding.calculateBottomPadding() + 8.dp)
                        ) {
                            LiveChatWidget()
                        }
                    }
                }
            }
        }
    }
}
