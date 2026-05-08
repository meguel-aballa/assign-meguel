package com.mambo.mywaterpay.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mambo.mywaterpay.Supabase
import com.mambo.mywaterpay.ui.theme.MyWaterPayTheme
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Color.Black // Catchy black background
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Catchy glow icon
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                modifier = Modifier.size(120.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = null,
                        modifier = Modifier.size(70.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "My Water Pay",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 2.sp
                )
            )

            Text(
                text = "Secure your water bills easily",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = MaterialTheme.shapes.medium,
                singleLine = true,
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
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    }
                },
                shape = MaterialTheme.shapes.medium,
                singleLine = true,
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

            TextButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Forgot Password?", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Please fill in all fields"
                        return@Button
                    }
                    
                    scope.launch {
                        try {
                            isLoading = true
                            errorMessage = null
                            Supabase.client.auth.signInWith(Email) {
                                this.email = email
                                this.password = password
                            }
                            onLoginSuccess()
                        } catch (e: Exception) {
                            errorMessage = e.message ?: "Login failed"
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
                    contentColor = Color.Black // High contrast
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
                    Text("LOGIN", fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Don't have an account?", color = Color.Gray)
                TextButton(onClick = { onSignUpClick() }, enabled = !isLoading) {
                    Text("Sign Up", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MyWaterPayTheme {
        LoginScreen()
    }
}
