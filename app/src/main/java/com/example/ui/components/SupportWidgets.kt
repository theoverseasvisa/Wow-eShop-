package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.Product
import com.example.ui.ShopViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Direct WhatsApp helper
fun launchWhatsApp(context: Context, text: String = "Hello Wow eShop, I am interested in inquiring about your premium products.") {
    val phone = "+919896008298"
    val url = "https://api.whatsapp.com/send?phone=$phone&text=${Uri.encode(text)}"
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp is not installed on this device.", Toast.LENGTH_SHORT).show()
    }
}

data class ChatMessage(val text: String, val isUser: Boolean, val timestamp: Long = System.currentTimeMillis())

@Composable
fun LiveChatWidget() {
    var expanded by remember { mutableStateOf(false) }
    var chatSessionStarted by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Namaste & Welcome to Wow eShop support! How can I assist your luxury shopping journey today?", false)
        )
    }
    
    var textInput by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(horizontalAlignment = Alignment.End) {
            
            // Support Panel
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
            ) {
                Card(
                    modifier = Modifier
                        .width(320.dp)
                        .height(420.dp)
                        .padding(bottom = 12.dp)
                        .testTag("live_chat_panel"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal),
                    elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Header
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(RoyalBluePrimary)
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(Color.Green, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "WOW Concierge",
                                            style = Typography.titleMedium.copy(color = Color.White)
                                        )
                                        Text(
                                            text = "Active Support Agent",
                                            style = Typography.bodyMedium.copy(color = HighContrastSilver, fontSize = 11.sp)
                                        )
                                    }
                                }
                                IconButton(onClick = { expanded = false }) {
                                    Icon(Icons.Default.Close, contentDescription = "Close chat", tint = Color.White)
                                }
                            }
                        }

                        // Chat messages
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            reverseLayout = false
                        ) {
                            items(messages) { msg ->
                                val alignment = if (msg.isUser) Alignment.End else Alignment.Start
                                val bubbleBg = if (msg.isUser) RoyalBluePrimary else Color(0xFF1E293B)
                                val textCol = if (msg.isUser) Color.White else SmoothWhite
                                
                                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
                                    Box(
                                        modifier = Modifier
                                            .clip(
                                                RoundedCornerShape(
                                                    topStart = 16.dp,
                                                    topEnd = 16.dp,
                                                    bottomStart = if (msg.isUser) 16.dp else 4.dp,
                                                    bottomEnd = if (msg.isUser) 4.dp else 16.dp
                                                )
                                            )
                                            .background(bubbleBg)
                                            .padding(horizontal = 14.dp, vertical = 10.dp)
                                    ) {
                                        Text(
                                            text = msg.text,
                                            style = Typography.bodyMedium.copy(color = textCol)
                                        )
                                    }
                                }
                            }
                        }

                        // Options quick buttons
                        if (messages.size == 1) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                listOf("Sizing Help", "Shipping Info", "WhatsApp Us").forEach { label ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFF1E293B))
                                            .clickable {
                                                messages.add(ChatMessage(label, true))
                                                scope.launch {
                                                    delay(1000)
                                                    val reply = when (label) {
                                                        "Sizing Help" -> "All luxury apparel is tailored to standard Indian fit sizes. Check measurements dynamically inside individual product specifications!"
                                                        "Shipping Info" -> "We offer FREE secure premium courier delivery across India for all transactions exceeding Rs 15,000. Under Rs 15,000, we apply a flat Rs 499 high-security logistics fee."
                                                        else -> "WhatsApp connected! Touch the green icon to launch our direct live WhatsApp dialog."
                                                    }
                                                    messages.add(ChatMessage(reply, false))
                                                }
                                            }
                                            .padding(horizontal = 8.dp, vertical = 6.dp)
                                    ) {
                                        Text(text = label, style = Typography.bodyMedium.copy(color = RoyalBlueGlow, fontSize = 11.sp))
                                    }
                                }
                            }
                        }

                        // Text Input
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = textInput,
                                onValueChange = { textInput = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .testTag("chat_input"),
                                placeholder = { Text("Inquire about luxury...", fontSize = 13.sp) },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF1E293B),
                                    unfocusedContainerColor = Color(0xFF1E293B),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(25.dp),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                keyboardActions = KeyboardActions(onSend = {
                                    if (textInput.isNotBlank()) {
                                        val userText = textInput
                                        messages.add(ChatMessage(userText, true))
                                        textInput = ""
                                        
                                        scope.launch {
                                            delay(1000)
                                            val autoReply = when {
                                                userText.contains("discount", ignoreCase = true) || userText.contains("coupon", ignoreCase = true) -> 
                                                    "A luxury offer is waiting! Apply coupon code 'WOWNEW' to get Rs 2,000 off, or use 'LUXURY20' for a 20% discount."
                                                userText.contains("price", ignoreCase = true) || userText.contains("cost", ignoreCase = true) -> 
                                                    "Wow eShop offers pure premium transparent pricing with active luxury warranties on all mechanical watches and biometric electronics."
                                                userText.contains("contact", ignoreCase = true) || userText.contains("phone", ignoreCase = true) -> 
                                                    "You can call or WhatsApp our official Indian executive desks directly at +91 98960 08298."
                                                else -> "Fantastic inquiry! I have flagged your ticket with our premium relationship manager. They will review your account soon."
                                            }
                                            messages.add(ChatMessage(autoReply, false))
                                        }
                                    }
                                })
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(RoyalBluePrimary, CircleShape)
                                    .clickable {
                                        if (textInput.isNotBlank()) {
                                            val userText = textInput
                                            messages.add(ChatMessage(userText, true))
                                            textInput = ""
                                            scope.launch {
                                                delay(1000)
                                                messages.add(ChatMessage("Your elite shopping inquiry has been recorded. Our Gurgaon representative will reply shortly.", false))
                                            }
                                        }
                                    }
                                    .testTag("chat_send_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }

            // Chat Floating Button
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(listOf(RoyalBluePrimary, RoyalBlueDeep)),
                        shape = CircleShape
                    )
                    .clickable { expanded = !expanded }
                    .testTag("live_chat_fab"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.Close else Icons.Default.Chat,
                    contentDescription = "Contact support",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
fun QuickViewProductDialog(
    product: Product,
    viewModel: ShopViewModel,
    onDismiss: () -> Unit
) {
    var selectedColor by remember { mutableStateOf(product.colorHexes.firstOrNull() ?: "#1E56FC") }
    var selectedSize by remember { mutableStateOf(product.sizes.firstOrNull() ?: "Standard") }
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .testTag("quick_view_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = LuxuryCharcoal)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quick View",
                        style = Typography.labelMedium.copy(color = RoyalBlueGlow)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = DustySlate)
                    }
                }

                // 3D Canvas visual preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LuxuryProductCanvas(
                        key = product.imageKey,
                        modifier = Modifier.size(150.dp),
                        primaryColor = Color(android.graphics.Color.parseColor(selectedColor))
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title details
                Text(
                    text = product.name,
                    style = Typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Rs. ${String.format("%,.2f", product.price)}",
                        style = Typography.titleMedium.copy(color = RoyalBlueGlow)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Rating", tint = MetallicGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${product.rating}", style = Typography.bodyMedium.copy(color = SmoothWhite))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Short description
                Text(
                    text = product.description,
                    style = Typography.bodyMedium.copy(color = DustySlate),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Size toggle
                Text(text = "Sizing", style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.White))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    product.sizes.forEach { sz ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selectedSize == sz) RoyalBluePrimary else Color(0xFF1E293B))
                                .clickable { selectedSize = sz }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(text = sz, style = Typography.bodyMedium.copy(color = Color.White))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Main CTA
                Button(
                    onClick = {
                        viewModel.addToCart(product.id, selectedColor, selectedSize)
                        Toast.makeText(context, "${product.name} appended to cart!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dialog_add_to_cart"),
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Add to Cart")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Add To Cart", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
