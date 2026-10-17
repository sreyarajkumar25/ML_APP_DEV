package com.yourname.mllearn.topics.mlp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import com.yourname.mllearn.data.AlgorithmConfig
import com.yourname.mllearn.ui.components.ControlSlider
import com.yourname.mllearn.ui.components.LessonResultCard
import com.yourname.mllearn.ui.components.LessonSection
import com.yourname.mllearn.ui.components.PlotCanvas
import com.yourname.mllearn.ui.components.PlotLine
import com.yourname.mllearn.ui.theme.LearnIndigo
import java.util.Locale

@Composable
fun MlpPlayground(config: AlgorithmConfig = AlgorithmConfig()) {
    var learningRate by remember(config) { mutableFloatStateOf(config.learningRate) }
    var epochs by remember(config) { mutableIntStateOf(config.iterations) }
    var hiddenUnits by remember(config) { mutableIntStateOf(config.neurons) }
    var trained by remember { mutableStateOf(true) }
    val result = remember(learningRate, epochs, hiddenUnits, config.hiddenLayers, trained) {
        MlpEngine.train(learningRate, if (trained) epochs else 1, hiddenUnits, config.hiddenLayers)
    }
    val maxLoss = (result.losses.maxOrNull() ?: 0.25f).coerceAtLeast(0.01f)
    val lossCurve = result.losses.mapIndexed { index, loss ->
        val x = if (result.losses.size < 2) 0f else index.toFloat() / (result.losses.lastIndex)
        x to (1f - (loss / maxLoss).coerceIn(0f, 1f))
    }

    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("Teach a tiny network XOR", style = MaterialTheme.typography.titleLarge)
            Text("Two input bits pass through a hidden layer. Backpropagation adjusts each weight to learn the XOR pattern.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            ControlSlider("Learning rate", learningRate, 0.05f..2f, { learningRate = it }, valueLabel = String.format(Locale.US, "%.2f", learningRate))
            ControlSlider("Training epochs", epochs.toFloat(), 20f..1000f, { epochs = it.toInt() }, steps = 48, valueLabel = epochs.toString())
            ControlSlider("Hidden units", hiddenUnits.toFloat(), 2f..8f, { hiddenUnits = it.toInt() }, steps = 5, valueLabel = hiddenUnits.toString())
        }
        item {
            Button(onClick = { trained = !trained }, modifier = Modifier.fillMaxWidth()) { Text(if (trained) "Preview one epoch" else "Train XOR for $epochs epochs") }
        }
        item {
            LessonSection("Training loss") {
                PlotCanvas(lines = listOf(PlotLine(lossCurve, LearnIndigo, 4f)))
                Text("Loss is sampled during backpropagation; use more epochs to continue learning.", style = MaterialTheme.typography.labelSmall)
            }
        }
        item {
            LessonResultCard("XOR network", listOf("Hidden layers: ${result.hiddenLayers}", "Neurons per layer: ${result.hiddenUnits}", "Epochs trained: ${result.epochs}", "Mean squared error: ${String.format(Locale.US, "%.4f", result.finalLoss)}"))
        }
    }
}
