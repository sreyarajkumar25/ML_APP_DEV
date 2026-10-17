package com.yourname.mllearn.topics.gradient_descent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yourname.mllearn.data.AlgorithmConfig
import com.yourname.mllearn.ui.components.ControlSlider
import com.yourname.mllearn.ui.components.PlotCanvas
import com.yourname.mllearn.ui.components.PlotLine
import com.yourname.mllearn.ui.components.PlotScatter
import com.yourname.mllearn.ui.theme.LearnIndigo
import com.yourname.mllearn.ui.theme.LearnOrange
import java.util.Locale

@Composable
fun GradientDescentPlayground(config: AlgorithmConfig = AlgorithmConfig()) {
    var startWeight by remember(config) { mutableFloatStateOf(config.startWeight) }
    var target by remember(config) { mutableFloatStateOf(config.target) }
    var learningRate by remember(config) { mutableFloatStateOf(config.learningRate) }
    var iterations by remember(config) { mutableIntStateOf(config.iterations) }
    var variant by remember(config) { mutableStateOf(DescentVariant.valueOf(config.variant)) }

    val path = remember(startWeight, target, learningRate, iterations, variant, config.batchSize) {
        GradientDescentEngine.optimize(startWeight, target, learningRate, iterations, variant, config.batchSize)
    }
    val curve = remember(target) { GradientDescentEngine.normalizedCurve(target) }
    val normalizedPath = remember(path, target) { GradientDescentEngine.normalizedPath(path, target) }
    val finalStep = path.last()

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Choose a starting point", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Watch the weight move toward the lowest point of the loss curve.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Update style", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DescentVariant.entries.forEach { option ->
                        FilterChip(
                            selected = variant == option,
                            onClick = { variant = option },
                            label = { Text(option.label) }
                        )
                    }
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.38f))) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                    ControlSlider("Start weight", startWeight, -5f..5f, { startWeight = it }, valueLabel = String.format(Locale.US, "%.2f", startWeight))
                    ControlSlider("Target", target, -4f..4f, { target = it }, valueLabel = String.format(Locale.US, "%.2f", target))
                    ControlSlider("Learning rate η", learningRate, 0.01f..0.95f, { learningRate = it }, valueLabel = String.format(Locale.US, "%.2f", learningRate))
                    ControlSlider("Iterations", iterations.toFloat(), 1f..60f, { iterations = it.toInt() }, steps = 58, valueLabel = iterations.toString())
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Loss landscape", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    PlotCanvas(
                        lines = listOf(PlotLine(curve, LearnIndigo, 4f)),
                        scatters = listOf(PlotScatter(normalizedPath, LearnOrange, 5f))
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("w = ${GradientDescentEngine.PLOT_MIN_WEIGHT.toInt()}", style = MaterialTheme.typography.labelSmall)
                        Text("● step path", style = MaterialTheme.typography.labelSmall, color = LearnOrange)
                        Text("w = ${GradientDescentEngine.PLOT_MAX_WEIGHT.toInt()}", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("After $iterations updates", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Weight: ${String.format(Locale.US, "%.3f", finalStep.weight)}", style = MaterialTheme.typography.bodyLarge)
                    Text("Loss: ${String.format(Locale.US, "%.4f", finalStep.loss)}", style = MaterialTheme.typography.bodyLarge)
                    Text("Update style: ${variant.label}${if (variant == DescentVariant.MINI_BATCH) " - batch size ${config.batchSize}" else ""}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
