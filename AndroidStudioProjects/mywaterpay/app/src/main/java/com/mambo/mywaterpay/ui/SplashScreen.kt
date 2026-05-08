package com.mambo.mywaterpay.ui

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "splash")

    // Pulsing scale for the main icon
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Rotation effect
    val rotation by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    // Ripple effect
    val rippleScale by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rippleScale"
    )
    val rippleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rippleAlpha"
    )

    // Entry animations
    val alphaAnim = remember { Animatable(0f) }
    val dropAnim = remember { Animatable(-200f) }

    LaunchedEffect(key1 = true) {
        launch {
            alphaAnim.animateTo(1f, animationSpec = tween(1200))
        }
        launch {
            dropAnim.animateTo(0f, animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            ))
        }
        delay(3000L)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // Catchy black background
        contentAlignment = Alignment.Center
    ) {
        // Gradient glow in the center
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Background Ripples
        Box(
            modifier = Modifier
                .size(100.dp)
                .scale(rippleScale)
                .alpha(rippleAlpha)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = dropAnim.value.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                // Glow icon
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = null,
                    modifier = Modifier
                        .size(160.dp)
                        .scale(scale * 1.1f)
                        .alpha(alphaAnim.value * 0.2f),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                // Main icon
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = null,
                    modifier = Modifier
                        .size(140.dp)
                        .scale(scale)
                        .graphicsLayer {
                            rotationZ = rotation
                        }
                        .alpha(alphaAnim.value),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Icon(
                    imageVector = Icons.Default.Waves,
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(top = 40.dp)
                        .alpha(alphaAnim.value * 0.5f),
                    tint = Color.White.copy(alpha = 0.7f)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "My Water Pay",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.alpha(alphaAnim.value)
            )
            
            Text(
                text = "Pure Convenience",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .alpha(alphaAnim.value * 0.8f)
            )
        }
    }
}
