package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*
import kotlin.math.sin

@Composable
fun LuxuryProductCanvas(
    key: String,
    modifier: Modifier = Modifier,
    primaryColor: Color = RoyalBluePrimary,
    accentColor: Color = MetallicGold
) {
    // Elegant infinite animations for 3D float/pulsing highlights
    val infiniteTransition = rememberInfiniteTransition(label = "LuxuryAnim")
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Pulse"
    )

    val translationY by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Float"
    )

    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Spin"
    )

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = translationY.dp)
        ) {
            val w = size.width
            val h = size.height
            val cx = w / 2
            val cy = h / 2
            val radius = size.minDimension / 3

            // Draw a elegant glowing light backdrop first
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(primaryColor.copy(alpha = 0.18f), Color.Transparent),
                    center = Offset(cx, cy),
                    radius = radius * 1.5f
                )
            )

            when (key) {
                "watch" -> {
                    // Strap
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            listOf(primaryColor.copy(alpha = 0.8f), RoyalBlueDeep)
                        ),
                        topLeft = Offset(cx - w * 0.12f, cy - h * 0.42f),
                        size = Size(w * 0.24f, h * 0.84f),
                        cornerRadius = CornerRadius(20f, 20f)
                    )
                    
                    // Case
                    drawCircle(
                        color = Color(0xFF1E293B),
                        center = Offset(cx, cy),
                        radius = radius
                    )
                    drawCircle(
                        brush = Brush.sweepGradient(
                            listOf(Color.White.copy(alpha = 0.1f), HighContrastSilver, Color.White.copy(alpha = 0.1f)),
                            center = Offset(cx, cy)
                        ),
                        center = Offset(cx, cy),
                        radius = radius,
                        style = Stroke(width = 8f)
                    )

                    // Carbon dial
                    drawCircle(
                        color = DeepBlackBg,
                        center = Offset(cx, cy),
                        radius = radius - 8f
                    )

                    // 3D Holographic circular neon ring
                    val ringRadius = radius * 0.7f * pulseScale
                    drawCircle(
                        brush = Brush.sweepGradient(
                            listOf(RoyalBlueGlow, Color.Transparent, RoyalBlueGlow.copy(alpha = 0.2f), RoyalBlueGlow),
                            center = Offset(cx, cy)
                        ),
                        center = Offset(cx, cy),
                        radius = ringRadius,
                        style = Stroke(width = 6f)
                    )

                    // Neon ticks
                    for (i in 0 until 12) {
                        val angleDeg = i * 30f
                        val angleRad = Math.toRadians(angleDeg.toDouble())
                        val cr = radius - 20f
                        val sx = cx + cr * Math.cos(angleRad).toFloat()
                        val sy = cy + cr * Math.sin(angleRad).toFloat()
                        drawCircle(
                            color = if (i % 3 == 0) RoyalBlueGlow else DustySlate,
                            center = Offset(sx, sy),
                            radius = if (i % 3 == 0) 5f else 3f
                        )
                    }

                    // Matrix code/telemetry
                    drawCircle(
                        color = RoyalBlueGlow.copy(alpha = 0.4f * pulseScale),
                        center = Offset(cx, cy),
                        radius = 12f
                    )
                }

                "jacket" -> {
                    val path = Path().apply {
                        moveTo(cx, cy - h * 0.35f) // collar back
                        lineTo(cx - w * 0.22f, cy - h * 0.22f) // left shoulder
                        lineTo(cx - w * 0.25f, cy + h * 0.28f) // left cuff
                        lineTo(cx - w * 0.15f, cy + h * 0.35f) // left waist
                        lineTo(cx, cy + h * 0.22f) // zip bottom
                        lineTo(cx + w * 0.15f, cy + h * 0.35f) // right waist
                        lineTo(cx + w * 0.25f, cy + h * 0.28f) // right cuff
                        lineTo(cx + w * 0.22f, cy - h * 0.22f) // right shoulder
                        close()
                    }
                    // Luxury premium leather body
                    drawPath(
                        path = path,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A), Color(0xFF020617)),
                            start = Offset(0f, 0f),
                            end = Offset(w, h)
                        )
                    )

                    // Asymmetric premium silver zippers
                    drawLine(
                        color = HighContrastSilver,
                        start = Offset(cx - w * 0.05f, cy - h * 0.12f),
                        end = Offset(cx + w * 0.08f, cy + h * 0.2f),
                        strokeWidth = 5f
                    )

                    // Thermal glowing red/gold neon filament paths
                    val filamentPath = Path().apply {
                        moveTo(cx - w * 0.1f, cy - h * 0.05f)
                        lineTo(cx - w * 0.12f, cy + h * 0.15f)
                        moveTo(cx + w * 0.08f, cy - h * 0.08f)
                        lineTo(cx + w * 0.12f, cy + h * 0.1f)
                    }
                    drawPath(
                        path = filamentPath,
                        color = ErrorRed.copy(alpha = 0.7f * pulseScale),
                        style = Stroke(width = 3.5f, cap = StrokeCap.Round)
                    )

                    // Metal buckle dots
                    drawCircle(
                        color = HighContrastSilver,
                        center = Offset(cx - w * 0.15f, cy - h * 0.15f),
                        radius = 5f
                    )
                    drawCircle(
                        color = HighContrastSilver,
                        center = Offset(cx + w * 0.15f, cy - h * 0.15f),
                        radius = 5f
                    )
                }

                "headphones" -> {
                    // Band outline
                    val uPath = Path().apply {
                        moveTo(cx - radius * 1.1f, cy)
                        cubicTo(
                            cx - radius * 1.1f, cy - radius * 1.2f,
                            cx + radius * 1.1f, cy - radius * 1.2f,
                            cx + radius * 1.1f, cy
                        )
                    }
                    drawPath(
                        path = uPath,
                        brush = Brush.linearGradient(
                            listOf(HighContrastSilver, accentColor, HighContrastSilver)
                        ),
                        style = Stroke(width = 10f, cap = StrokeCap.Round)
                    )

                    // Floating blue transducer arcs
                    val pulseRadius = radius * pulseScale
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(RoyalBlueGlow, Color.Transparent),
                            center = Offset(cx - radius * 1.1f, cy),
                            radius = 35f
                        )
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(RoyalBlueGlow, Color.Transparent),
                            center = Offset(cx + radius * 1.1f, cy),
                            radius = 35f
                        )
                    )

                    // Core acoustic ear pod cups
                    drawRoundRect(
                        color = Color(0xFF1E293B),
                        topLeft = Offset(cx - radius * 1.22f, cy - 25f),
                        size = Size(35f, 65f),
                        cornerRadius = CornerRadius(12f, 12f)
                    )
                    drawRoundRect(
                        color = Color(0xFF1E293B),
                        topLeft = Offset(cx + radius * 1.1f, cy - 25f),
                        size = Size(35f, 65f),
                        cornerRadius = CornerRadius(12f, 12f)
                    )

                    // Silver accents on cups
                    drawRoundRect(
                        color = accentColor,
                        topLeft = Offset(cx - radius * 1.15f, cy - 10f),
                        size = Size(8f, 35f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    drawRoundRect(
                        color = accentColor,
                        topLeft = Offset(cx + radius * 1.11f, cy - 10f),
                        size = Size(8f, 35f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }

                "humidifier" -> {
                    // Glass Cylinder Chassis
                    val glassRectTopLeft = Offset(cx - w * 0.24f, cy - h * 0.35f)
                    val glassSize = Size(w * 0.48f, h * 0.65f)
                    drawRoundRect(
                        color = LuxuryCharcoal.copy(alpha = 0.6f),
                        topLeft = glassRectTopLeft,
                        size = glassSize,
                        cornerRadius = CornerRadius(30f, 30f)
                    )
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            listOf(Color.White.copy(alpha = 0.4f), Color.White.copy(alpha = 0.05f))
                        ),
                        topLeft = glassRectTopLeft,
                        size = glassSize,
                        cornerRadius = CornerRadius(30f, 30f),
                        style = Stroke(width = 4f)
                    )

                    // Metallic pedestal plate
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            listOf(HighContrastSilver, Color(0xFF1E293B))
                        ),
                        topLeft = Offset(cx - w * 0.26f, cy + h * 0.25f),
                        size = Size(w * 0.52f, h * 0.12f),
                        cornerRadius = CornerRadius(15f, 15f)
                    )

                    // Floating reverse-gravity Purifying water droplets
                    val dropletAnim = (spinAngle % 100) / 100f // Loop 0f -> 1f
                    val dropletY1 = cy + h * 0.15f - (dropletAnim * h * 0.45f)
                    val dropletY2 = cy + h * 0.15f - (((dropletAnim + 0.5f) % 1f) * h * 0.45f)

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(RoyalBlueGlow, Color.White),
                            center = Offset(cx, dropletY1),
                            radius = 12f
                        ),
                        center = Offset(cx, dropletY1),
                        radius = 12f
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(RoyalBlueGlow, Color.White),
                            center = Offset(cx - 30f, dropletY2),
                            radius = 9f
                        ),
                        center = Offset(cx - 30f, dropletY2),
                        radius = 9f
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(RoyalBlueGlow, Color.White),
                            center = Offset(cx + 30f, dropletY2),
                            radius = 6f
                        ),
                        center = Offset(cx + 30f, dropletY2),
                        radius = 6f
                    )

                    // Core atomizing ultrasonic silver nozzle at bottom
                    drawCircle(
                        color = accentColor,
                        center = Offset(cx, cy + h * 0.23f),
                        radius = 14f
                    )
                }

                "perfume" -> {
                    // Faceted Crystal Flask Body
                    val prismPath = Path().apply {
                        moveTo(cx, cy - h * 0.35f) // top nozzle neck
                        lineTo(cx + w * 0.2f, cy - h * 0.15f) // right edge top
                        lineTo(cx + w * 0.23f, cy + h * 0.22f) // right edge bottom
                        lineTo(cx - w * 0.23f, cy + h * 0.22f) // left edge bottom
                        lineTo(cx - w * 0.2f, cy - h * 0.15f) // left edge top
                        close()
                    }
                    drawPath(
                        path = prismPath,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF0F172A), DeepBlackBg, Color(0xFF334155)),
                            start = Offset(0f, 0f),
                            end = Offset(w, h)
                        )
                    )

                    // Facets reflecting gold/silver
                    val facetLine1 = Path().apply {
                        moveTo(cx, cy - h * 0.35f)
                        lineTo(cx, cy + h * 0.22f)
                    }
                    drawPath(facetLine1, accentColor.copy(alpha = 0.5f), style = Stroke(width = 3f))

                    val facetLine2 = Path().apply {
                        moveTo(cx - w * 0.2f, cy - h * 0.15f)
                        lineTo(cx, cy + h * 0.05f)
                        lineTo(cx + w * 0.2f, cy - h * 0.15f)
                    }
                    drawPath(facetLine2, Color.White.copy(alpha = 0.18f), style = Stroke(width = 3.2f))

                    // Premium Gold Nozzle Spray Cap
                    drawRoundRect(
                        color = accentColor,
                        topLeft = Offset(cx - w * 0.08f, cy - h * 0.44f),
                        size = Size(w * 0.16f, h * 0.1f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                    drawRoundRect(
                        color = Color(0xFF1E293B),
                        topLeft = Offset(cx - w * 0.05f, cy - h * 0.48f),
                        size = Size(w * 0.1f, h * 0.05f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )

                    // Elegant brand script plate centered
                    drawRoundRect(
                        color = DeepBlackBg.copy(alpha = 0.9f),
                        topLeft = Offset(cx - w * 0.14f, cy - 20f),
                        size = Size(w * 0.28f, 45f),
                        cornerRadius = CornerRadius(5f, 5f)
                    )
                    drawRoundRect(
                        color = accentColor,
                        topLeft = Offset(cx - w * 0.14f, cy - 20f),
                        size = Size(w * 0.28f, 45f),
                        cornerRadius = CornerRadius(5f, 5f),
                        style = Stroke(width = 2f)
                    )
                }

                "mechanical_watch" -> {
                    // Gear system behind (rotating!)
                    val gearRadius = radius * 0.9f
                    drawCircle(
                        brush = Brush.sweepGradient(
                            listOf(Color(0xFF334155), Color(0xFF1E293B), Color(0xFF334155))
                        ),
                        center = Offset(cx, cy),
                        radius = gearRadius,
                        style = Stroke(width = 12f)
                    )

                    // Draw gear teeth based on spinAngle
                    val teethCount = 18
                    for (i in 0 until teethCount) {
                        val angle = (i * (360f / teethCount)) + spinAngle
                        val angleRad = Math.toRadians(angle.toDouble())
                        val tx = cx + (gearRadius) * Math.cos(angleRad).toFloat()
                        val ty = cy + (gearRadius) * Math.sin(angleRad).toFloat()
                        drawCircle(
                            color = Color(0xFF475569),
                            center = Offset(tx, ty),
                            radius = 6f
                        )
                    }

                    // Rotating golden tourbillon weight
                    val tourbAngle = -spinAngle * 1.5f
                    val tourbRad = Math.toRadians(tourbAngle.toDouble())
                    val watchArmX = cx + (radius * 0.42f) * Math.cos(tourbRad).toFloat()
                    val watchArmY = cy + (radius * 0.42f) * Math.sin(tourbRad).toFloat()

                    drawLine(
                        color = accentColor,
                        start = Offset(cx, cy),
                        end = Offset(watchArmX, watchArmY),
                        strokeWidth = 6f,
                        cap = StrokeCap.Round
                    )
                    drawCircle(
                        color = accentColor,
                        center = Offset(watchArmX, watchArmY),
                        radius = 20f
                    )
                    drawCircle(
                        color = DeepBlackBg,
                        center = Offset(watchArmX, watchArmY),
                        radius = 12f
                    )

                    // Outer Titanium ring
                    drawCircle(
                        color = Color.Transparent,
                        center = Offset(cx, cy),
                        radius = radius,
                        style = Stroke(width = 12f)
                    )
                    drawCircle(
                        brush = Brush.linearGradient(
                            listOf(HighContrastSilver, Color(0xFF1E293B), HighContrastSilver)
                        ),
                        center = Offset(cx, cy),
                        radius = radius,
                        style = Stroke(width = 6f)
                    )
                }

                "drone" -> {
                    // X Arms
                    val dArmLen = radius * 1.1f
                    drawLine(
                        color = Color(0xFF334155),
                        start = Offset(cx - dArmLen, cy - dArmLen),
                        end = Offset(cx + dArmLen, cy + dArmLen),
                        strokeWidth = 14f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = Color(0xFF475569),
                        start = Offset(cx - dArmLen, cy + dArmLen),
                        end = Offset(cx + dArmLen, cy - dArmLen),
                        strokeWidth = 14f,
                        cap = StrokeCap.Round
                    )

                    // Spin propellers at arm tips
                    drawCircle(color = primaryColor, center = Offset(cx - dArmLen, cy - dArmLen), radius = 18f)
                    drawCircle(color = primaryColor, center = Offset(cx + dArmLen, cy + dArmLen), radius = 18f)
                    drawCircle(color = primaryColor, center = Offset(cx - dArmLen, cy + dArmLen), radius = 18f)
                    drawCircle(color = primaryColor, center = Offset(cx + dArmLen, cy - dArmLen), radius = 18f)

                    // Rotating propeller blade indicators
                    val propA = Math.toRadians(spinAngle.toDouble() * 3f)
                    val pScale = 22f
                    listOf(
                        Offset(cx - dArmLen, cy - dArmLen),
                        Offset(cx + dArmLen, cy + dArmLen),
                        Offset(cx - dArmLen, cy + dArmLen),
                        Offset(cx + dArmLen, cy - dArmLen)
                    ).forEach { offset ->
                        val px1 = offset.x + pScale * Math.cos(propA).toFloat()
                        val py1 = offset.y + pScale * Math.sin(propA).toFloat()
                        val px2 = offset.x + pScale * Math.cos(propA + Math.PI).toFloat()
                        val py2 = offset.y + pScale * Math.sin(propA + Math.PI).toFloat()
                        drawLine(
                            color = HighContrastSilver,
                            start = Offset(px1, py1),
                            end = Offset(px2, py2),
                            strokeWidth = 4f,
                            cap = StrokeCap.Round
                        )
                    }

                    // Sphere Quad-camera body
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF0F172A), DeepBlackBg),
                            center = Offset(cx, cy),
                            radius = radius * 0.7f
                        ),
                        center = Offset(cx, cy),
                        radius = radius * 0.7f
                    )
                    drawCircle(
                        color = accentColor,
                        center = Offset(cx, cy + 12f),
                        radius = 11f
                    )
                }

                "table" -> {
                    // Walnut Table top perspective polygon
                    val pWidth = w * 0.42f
                    val pHeight = h * 0.28f
                    val topPath = Path().apply {
                        moveTo(cx - pWidth * 0.8f, cy - pHeight)
                        lineTo(cx + pWidth * 0.8f, cy - pHeight)
                        lineTo(cx + pWidth * 1.1f, cy + pHeight)
                        lineTo(cx - pWidth * 1.1f, cy + pHeight)
                        close()
                    }
                    // Solid dark walnut wood color
                    drawPath(
                        path = topPath,
                        brush = Brush.linearGradient(
                            listOf(Color(0xFF5D2502), Color(0xFF3B1601), Color(0xFF702F05))
                        )
                    )

                    // OLED glass floating display outline
                    val topGlass = Path().apply {
                        moveTo(cx - pWidth * 0.6f, cy - pHeight * 0.6f)
                        lineTo(cx + pWidth * 0.6f, cy - pHeight * 0.6f)
                        lineTo(cx + pWidth * 0.8f, cy + pHeight * 0.6f)
                        lineTo(cx - pWidth * 0.8f, cy + pHeight * 0.6f)
                        close()
                    }
                    drawPath(
                        path = topGlass,
                        brush = Brush.linearGradient(
                            listOf(RoyalBluePrimary.copy(alpha = 0.5f), RoyalBlueDeep.copy(alpha = 0.9f))
                        )
                    )
                    drawPath(
                        path = topGlass,
                        color = RoyalBlueGlow.copy(alpha = 0.8f),
                        style = Stroke(width = 3.5f)
                    )

                    // Table metal modern legs (legs descending from bottom edge of table top)
                    drawLine(color = HighContrastSilver, start = Offset(cx - pWidth * 0.9f, cy + pHeight), end = Offset(cx - pWidth * 1.1f, cy + h * 0.38f), strokeWidth = 10f, cap = StrokeCap.Round)
                    drawLine(color = HighContrastSilver, start = Offset(cx + pWidth * 0.9f, cy + pHeight), end = Offset(cx + pWidth * 1.1f, cy + h * 0.38f), strokeWidth = 10f, cap = StrokeCap.Round)
                    drawLine(color = Color(0xFF475569), start = Offset(cx - pWidth * 0.6f, cy - pHeight), end = Offset(cx - pWidth * 0.7f, cy + h * 0.1f), strokeWidth = 6f, cap = StrokeCap.Round)
                    drawLine(color = Color(0xFF475569), start = Offset(cx + pWidth * 0.6f, cy - pHeight), end = Offset(cx + pWidth * 0.7f, cy + h * 0.1f), strokeWidth = 6f, cap = StrokeCap.Round)
                }

                else -> {
                    // Fallback generic premium geometry
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(primaryColor, Color.Transparent)
                        ),
                        center = Offset(cx, cy),
                        radius = radius * pulseScale
                    )
                }
            }
        }
    }
}
