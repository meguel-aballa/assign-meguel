package com.mambo.mywaterpay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.mambo.mywaterpay.models.WaterMeter
import com.mambo.mywaterpay.ui.*
import com.mambo.mywaterpay.ui.theme.MyWaterPayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Force dynamicColor to false to ensure our theme colors are used
            MyWaterPayTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

enum class Screen {
    Splash, Login, SignUp, ForgotPassword, Home, Shop, Cart
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf(Screen.Splash) }
    val cartItems = remember { mutableStateListOf<WaterMeter>() }

    when (currentScreen) {
        Screen.Splash -> SplashScreen(
            onSplashFinished = { currentScreen = Screen.Login }
        )
        Screen.Login -> LoginScreen(
            onLoginSuccess = { currentScreen = Screen.Home },
            onSignUpClick = { currentScreen = Screen.SignUp },
            onForgotPasswordClick = { currentScreen = Screen.ForgotPassword }
        )
        Screen.SignUp -> SignUpScreen(
            onSignUpSuccess = { currentScreen = Screen.Login },
            onBackClick = { currentScreen = Screen.Login }
        )
        Screen.ForgotPassword -> ForgotPasswordScreen(
            onBackClick = { currentScreen = Screen.Login },
            onResetSent = { currentScreen = Screen.Login }
        )
        Screen.Home -> WaterPaymentScreen(
            onLogout = { currentScreen = Screen.Login },
            onNavigateToShop = { currentScreen = Screen.Shop }
        )
        Screen.Shop -> WaterMeterShopScreen(
            onBackClick = { currentScreen = Screen.Home },
            onAddToCart = { meter -> cartItems.add(meter) },
            cartItemCount = cartItems.size,
            onCartClick = { currentScreen = Screen.Cart }
        )
        Screen.Cart -> CartScreen(
            cartItems = cartItems,
            onRemoveItem = { meter -> cartItems.remove(meter) },
            onBackClick = { currentScreen = Screen.Shop },
            onCheckout = {
                // For now, clear cart and go back home on success
                cartItems.clear()
                currentScreen = Screen.Home
            }
        )
    }
}
