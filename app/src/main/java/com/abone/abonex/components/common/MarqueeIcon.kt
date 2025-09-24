package com.abone.abonex.components.common

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun MarqueeIcon(
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current,
    durationMillis: Int = 1500
) {
    var containerWidthPx by remember { mutableIntStateOf(0) }

    val iconWidthPx = with(LocalDensity.current) { 24.dp.toPx() }

    val infiniteTransition = rememberInfiniteTransition(label = "iconMarquee")

    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = containerWidthPx.toFloat() - iconWidthPx,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iconMarqueeOffset"
    )

    Box(
        modifier = modifier
            .onSizeChanged {
                containerWidthPx = it.width
            }
            .clipToBounds()
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier
                .offset { IntOffset(offset.toInt(), 0) }
                .size(24.dp)
        )
    }
}