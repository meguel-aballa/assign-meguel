package com.mambo.mywaterpay.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mambo.mywaterpay.models.WaterMeter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterMeterShopScreen(
    onBackClick: () -> Unit,
    onAddToCart: (WaterMeter) -> Unit,
    onCartClick: () -> Unit,
    cartItemCount: Int
) {
    val meters = remember {
        listOf(
            WaterMeter(
                id = "1",
                name = "Smart Flow X100",
                description = "Digital precision with real-time leak detection and Wi-Fi connectivity.",
                price = 149.99,
                imageUrl = "https://images.unsplash.com/photo-1585333081030-6456eb883a81?q=80&w=400"
            ),
            WaterMeter(
                id = "2",
                name = "EcoPulse G2",
                description = "Battery-powered analog-to-digital converter for high durability.",
                price = 89.50,
                imageUrl = "https://images.unsplash.com/photo-1581244277943-fe4a9c777189?q=80&w=400"
            ),
            WaterMeter(
                id = "3",
                name = "HydroGuard Pro",
                description = "Industrial grade water meter for large estates and commercial use.",
                price = 299.00,
                imageUrl = "https://images.unsplash.com/photo-1590425330310-863a89e9f3f4?q=80&w=400"
            ),
            WaterMeter(
                id = "4",
                name = "MiniMeter Plus",
                description = "Compact design perfect for apartments and small spaces.",
                price = 45.00,
                imageUrl = "https://images.unsplash.com/photo-1585333142013-08579d460e4e?q=80&w=400"
            )
        )
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text("Water Meter Shop", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.secondary
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onCartClick) {
                        BadgedBox(
                            badge = {
                                if (cartItemCount > 0) {
                                    Badge(containerColor = MaterialTheme.colorScheme.secondary) {
                                        Text(cartItemCount.toString(), color = Color.Black)
                                    }
                                }
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    text = "Featured Products",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(meters) { meter ->
                WaterMeterCard(meter = meter, onAddToCart = { onAddToCart(meter) })
            }
        }
    }
}

@Composable
fun WaterMeterCard(meter: WaterMeter, onAddToCart: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF191C1D)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = meter.imageUrl,
                contentDescription = meter.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
            
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = meter.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$${meter.price}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = meter.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onAddToCart,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ADD TO CART", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
