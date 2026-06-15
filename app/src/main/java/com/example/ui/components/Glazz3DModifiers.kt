package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LuxuryGlassOverlay
import com.example.ui.theme.PremiumSilverBorder
import com.example.ui.theme.RoyalBlueGlow

@Composable
fun Modifier.glassmorphic(
    shape: Shape = RoundedCornerShape(20.dp),
    borderWidth: Dp = 1.dp,
    elevation: Dp = 10.dp,
    glowAlpha: Float = 0.12f
): Modifier = this.then(
    Modifier
        .shadow(
            elevation = elevation,
            shape = shape,
            clip = false,
            ambientColor = Color.Black,
            spotColor = RoyalBlueGlow.copy(alpha = glowAlpha)
        )
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    LuxuryGlassOverlay,
                    Color(0x7F080A0F)
                )
            ),
            shape = shape
        )
        .border(
            width = borderWidth,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.22f),
                    PremiumSilverBorder.copy(alpha = 0.3f),
                    RoyalBlueGlow.copy(alpha = 0.4f),
                    Color.Transparent
                )
            ),
            shape = shape
        )
        .clip(shape)
)

fun Modifier.tilt3D(
    enabled: Boolean = true,
    maxRotation: Float = 16f
): Modifier = composed {
    if (!enabled) return@composed this

    // Track touch/drag offset to tilt
    var tiltX by remember { mutableStateOf(0f) }
    var tiltY by remember { mutableStateOf(0f) }

    // Smooth return transition when user releases pointer
    val animatedTiltX by animateFloatAsState(
        targetValue = tiltX,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow),
        label = "TiltXAnim"
    )
    val animatedTiltY by animateFloatAsState(
        targetValue = tiltY,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow),
        label = "TiltYAnim"
    )

    // Combine pointer input tracking with 3D graphics rotation matrix
    this
        .pointerInput(Unit) {
            detectDragGestures(
                onDrag = { change, dragAmount ->
                    change.consume()
                    // Map horizontal offset to Y-axis rotation, vertical to X-axis
                    val deltaX = (tiltX + dragAmount.x / 14f).coerceIn(-maxRotation, maxRotation)
                    val deltaY = (tiltY - dragAmount.y / 14f).coerceIn(-maxRotation, maxRotation)
                    tiltX = deltaX
                    tiltY = deltaY
                },
                onDragEnd = {
                    tiltX = 0f
                    tiltY = 0f
                },
                onDragCancel = {
                    tiltX = 0f
                    tiltY = 0f
                }
            )
        }
        .graphicsLayer {
            rotationY = animatedTiltX
            rotationX = animatedTiltY
            cameraDistance = 16f * density // 3D Camera depth matrix multiplier
        }
}

@Composable
fun Modifier.subtleFloating(
    durationMillis: Int = 3500
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "FloatModifier")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FloatOffset"
    )
    this.offset(y = offsetY.dp)
}
