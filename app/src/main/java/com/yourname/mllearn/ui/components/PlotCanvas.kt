package com.yourname.mllearn.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

private val PlotGrid = Color(0xFFE0E0E0)

 data class PlotLine(
    val points: List<Pair<Float, Float>>,
    val color: Color,
    val width: Float = 3f
)

data class PlotScatter(
    val points: List<Pair<Float, Float>>,
    val color: Color,
    val radius: Float = 5f
)

@Composable
fun PlotCanvas(
    lines: List<PlotLine>,
    scatters: List<PlotScatter> = emptyList(),
    modifier: Modifier = Modifier,
    gridColor: Color = PlotGrid
) {
    Canvas(modifier = modifier.fillMaxWidth().height(220.dp)) {
        val horizontalSteps = 4
        val verticalSteps = 6
        repeat(horizontalSteps + 1) { index ->
            val y = size.height * index / horizontalSteps
            drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
        }
        repeat(verticalSteps + 1) { index ->
            val x = size.width * index / verticalSteps
            drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f)
        }
        lines.forEach { drawLine(it.points, it.color, it.width) }
        scatters.forEach { drawScatter(it.points, it.color, it.radius) }
    }
}

fun DrawScope.drawLine(points: List<Pair<Float, Float>>, color: Color, width: Float) {
    if (points.size < 2) return
    val path = Path().apply {
        points.forEachIndexed { index, point ->
            val x = point.first.coerceIn(0f, 1f) * size.width
            val y = (1f - point.second.coerceIn(0f, 1f)) * size.height
            if (index == 0) moveTo(x, y) else lineTo(x, y)
        }
    }
    drawPath(path, color, style = Stroke(width = width, cap = StrokeCap.Round))
}

fun DrawScope.drawScatter(points: List<Pair<Float, Float>>, color: Color, radius: Float) {
    points.forEach { point ->
        val x = point.first.coerceIn(0f, 1f) * size.width
        val y = (1f - point.second.coerceIn(0f, 1f)) * size.height
        drawCircle(color, radius, Offset(x, y))
    }
}
