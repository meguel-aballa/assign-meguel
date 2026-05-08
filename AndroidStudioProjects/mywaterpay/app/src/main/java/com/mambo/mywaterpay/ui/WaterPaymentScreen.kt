package com.mambo.mywaterpay.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mambo.mywaterpay.Supabase
import com.mambo.mywaterpay.ui.theme.MyWaterPayTheme
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Payment(
    @SerialName("account_number")
    val accountNumber: String,
    val amount: Double,
    val status: String = "completed"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterPaymentScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    onNavigateToShop: () -> Unit = {}
) {
    var accountNumberInput by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var isPaymentSuccessful by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text("Water Pay", fontWeight = FontWeight.ExtraBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = onNavigateToShop) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Shop Meters",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = {
                        scope.launch {
                            try {
                                Supabase.client.auth.signOut()
                                onLogout()
                            } catch (e: Exception) {
                                onLogout()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = modifier.fillMaxSize().padding(innerPadding)) {
            // Main Input Form
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    modifier = Modifier.size(100.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Bill Payment",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )
                )

                Text(
                    text = "Enter your details to pay your water bill",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = accountNumberInput,
                    onValueChange = { accountNumberInput = it; errorMessage = null },
                    label = { Text("Account Number") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = MaterialTheme.shapes.medium,
                    enabled = !isLoading,
                    isError = errorMessage != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = Color.Gray,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.DarkGray
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = amountInput,
                    onValueChange = { amountInput = it; errorMessage = null },
                    label = { Text("Amount to Pay") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    prefix = { Text("$ ", color = MaterialTheme.colorScheme.secondary) },
                    shape = MaterialTheme.shapes.medium,
                    enabled = !isLoading,
                    isError = errorMessage != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary,
                        unfocusedLabelColor = Color.Gray,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = Color.DarkGray
                    )
                )

                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp).align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = {
                        val amountDouble = amountInput.toDoubleOrNull()
                        if (accountNumberInput.isBlank() || amountDouble == null) {
                            errorMessage = "Please enter a valid account number and amount"
                            return@Button
                        }

                        scope.launch {
                            try {
                                isLoading = true
                                errorMessage = null
                                
                                val payment = Payment(
                                    accountNumber = accountNumberInput,
                                    amount = amountDouble
                                )
                                
                                Supabase.client.from("payments").insert(payment)
                                
                                isPaymentSuccessful = true
                            } catch (e: Exception) {
                                errorMessage = e.message ?: "Payment failed. Please check your connection."
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.Black
                    ),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.Black,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("PAY NOW", fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onNavigateToShop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.large,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                    enabled = !isLoading
                ) {
                    Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("BUY A WATER METER", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Success Post-Payment View
            AnimatedVisibility(
                visible = isPaymentSuccessful,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                Surface(
                    color = Color.Black,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(100.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Text(
                            text = "Payment Successful!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "Your water bill for account $accountNumberInput has been paid successfully.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                        
                        Spacer(modifier = Modifier.height(48.dp))
                        
                        // Option 1: Just go back to see the form
                        Button(
                            onClick = { isPaymentSuccessful = false },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = MaterialTheme.shapes.large,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.Black)
                        ) {
                            Text("GO BACK", fontWeight = FontWeight.Bold)
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))

                        // Option 2: Pay another bill (clears fields)
                        Button(
                            onClick = {
                                isPaymentSuccessful = false
                                accountNumberInput = ""
                                amountInput = ""
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = MaterialTheme.shapes.large,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary, contentColor = Color.Black)
                        ) {
                            Text("PAY AGAIN", fontWeight = FontWeight.Bold)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Option 3: Logout
                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    try {
                                        Supabase.client.auth.signOut()
                                        onLogout()
                                    } catch (e: Exception) {
                                        onLogout()
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = MaterialTheme.shapes.large,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("LOGOUT", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WaterPaymentScreenPreview() {
    MyWaterPayTheme {
        WaterPaymentScreen()
    }
}
