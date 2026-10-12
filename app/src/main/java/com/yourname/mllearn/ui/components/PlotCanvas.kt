package com.yourname.mllearn.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme

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
    gridColor: Color? = null,
    plotHeight: Dp = 220.dp
) {
    val activeGridColor = gridColor ?: if (MaterialTheme.colorScheme.surface.luminance() < 0.5f) {
        Color(0xFF424242)
    } else {
        PlotGrid
    }
    val previousLines = remember { mutableStateOf(lines) }
    val previousScatters = remember { mutableStateOf(scatters) }
    val progress = remember { Animatable(1f) }
    LaunchedEffect(lines, scatters) {
        progress.snapTo(0f)
        progress.animateTo(1f, animationSpec = tween(durationMillis = 320))
        previousLines.value = lines
        previousScatters.value = scatters
    }

    Canvas(modifier = modifier.fillMaxWidth().height(plotHeight)) {
        val horizontalSteps = 4
        val verticalSteps = 6
        repeat(horizontalSteps + 1) { index ->
            val y = size.height * index / horizontalSteps
            drawLine(activeGridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
        }
        repeat(verticalSteps + 1) { index ->
            val x = size.width * index / verticalSteps
            drawLine(activeGridColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f)
        }
        lines.forEachIndexed { index, line ->
            val old = previousLines.value.getOrNull(index)?.points
            val points = if (old?.size == line.points.size) {
                line.points.mapIndexed { pointIndex, point ->
                    val from = old[pointIndex]
                    val fraction = progress.value
                    (from.first + (point.first - from.first) * fraction) to
                        (from.second + (point.second - from.second) * fraction)
                }
            } else {
                line.points.take((line.points.size * progress.value).toInt().coerceAtLeast(2))
            }
            drawLine(points, line.color, line.width)
        }
        scatters.forEachIndexed { index, scatter ->
            val old = previousScatters.value.getOrNull(index)?.points
            val points = if (old?.size == scatter.points.size) {
                scatter.points.mapIndexed { pointIndex, point ->
                    val from = old[pointIndex]
                    val fraction = progress.value
                    (from.first + (point.first - from.first) * fraction) to
                        (from.second + (point.second - from.second) * fraction)
                }
            } else {
                scatter.points
            }
            drawScatter(points, scatter.color, scatter.radius)
        }
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
