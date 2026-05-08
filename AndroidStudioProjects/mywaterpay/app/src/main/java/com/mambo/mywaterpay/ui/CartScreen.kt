package com.mambo.mywaterpay.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mambo.mywaterpay.models.WaterMeter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<WaterMeter>,
    onRemoveItem: (WaterMeter) -> Unit,
    onBackClick: () -> Unit,
    onCheckout: () -> Unit
) {
    val totalPrice = cartItems.sumOf { it.price }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text("Your Cart", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Your cart is empty", color = Color.Gray, fontSize = 18.sp)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemRow(item = item, onRemove = { onRemoveItem(item) })
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF191C1D),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    tonalElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total", color = Color.White, fontSize = 20.sp)
                            Text(
                                text = "$${String.format("%.2f", totalPrice)}",
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = onCheckout,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(Icons.Default.ShoppingCartCheckout, contentDescription = null)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("PROCEED TO CHECKOUT", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: WaterMeter, onRemove: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF191C1D)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.name, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text(text = "$${item.price}", color = MaterialTheme.colorScheme.secondary)
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
