package com.abone.abonex.components

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class CurvedBottomBarShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = createPath(size,density)
        )
    }

    private fun createPath(size: Size,density: Density): Path {
        val path = Path()
        val fabAreaWidth = 70.dp.value * density.density
        val fabRadius = fabAreaWidth / 2f

        val curveDepth = 25.dp.value * density.density

        val startX = (size.width / 2f) - fabRadius - 10.dp.value * density.density
        val endX = (size.width / 2f) + fabRadius + 10.dp.value * density.density

        path.moveTo(0f, 0f)
        path.lineTo(startX, 0f)

        path.cubicTo(
            x1 = startX + (fabRadius * 0.3f),
            y1 = 0f,
            x2 = (size.width / 2f) - (fabRadius * 0.7f),
            y2 = curveDepth,
            x3 = size.width / 2f,
            y3 = curveDepth
        )

        path.cubicTo(
            x1 = (size.width / 2f) + (fabRadius * 0.7f),
            y1 = curveDepth,
            x2 = endX - (fabRadius * 0.3f),
            y2 = 0f,
            x3 = endX,
            y3 = 0f
        )

        path.lineTo(size.width, 0f)
        path.lineTo(size.width, size.height)
        path.lineTo(0f, size.height)
        path.close()

        return path
    }
}