package com.yourname.mllearn.topics.perceptron

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import com.yourname.mllearn.ui.components.ControlSlider
import com.yourname.mllearn.ui.components.LessonResultCard
import com.yourname.mllearn.ui.components.LessonSection
import com.yourname.mllearn.ui.components.PlotCanvas
import com.yourname.mllearn.ui.components.PlotLine
import com.yourname.mllearn.ui.components.PlotScatter
import com.yourname.mllearn.ui.theme.LearnIndigo
import com.yourname.mllearn.ui.theme.LearnOrange
import com.yourname.mllearn.ui.theme.LearnTeal
import java.util.Locale

@Composable
fun PerceptronPlayground() {
    var learningRate by remember { mutableFloatStateOf(0.2f) }
    var epochs by remember { mutableIntStateOf(8) }
    var trained by remember { mutableStateOf(true) }
    val model = remember(learningRate, epochs, trained) { PerceptronEngine.train(learningRate, if (trained) epochs else 1) }
    val misclassified = PerceptronEngine.data.filter { point ->
        (if (model.weightX * point.x + model.weightY * point.y + model.bias >= 0f) 1 else -1) != point.label
    }

    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("Correct mistakes one at a time", style = MaterialTheme.typography.titleLarge)
            Text("When a point lands on the wrong side, the perceptron nudges its weights toward the true class.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            ControlSlider("Learning rate", learningRate, 0.02f..0.8f, { learningRate = it }, valueLabel = String.format(Locale.US, "%.2f", learningRate))
            ControlSlider("Epochs", epochs.toFloat(), 1f..30f, { epochs = it.toInt() }, steps = 28, valueLabel = epochs.toString())
        }
        item {
            FilterChip(trained, onClick = { trained = !trained }, label = { Text(if (trained) "Trained model" else "One-epoch preview") })
        }
        item {
            LessonSection("Decision boundary") {
                PlotCanvas(
                    lines = listOf(PlotLine(PerceptronEngine.boundary(model), LearnIndigo, 4f)),
                    scatters = listOf(
                        PlotScatter(PerceptronEngine.data.filter { it.label == -1 }.map { it.x to it.y }, LearnTeal, 6f),
                        PlotScatter(PerceptronEngine.data.filter { it.label == 1 }.map { it.x to it.y }, LearnOrange, 6f),
                        PlotScatter(misclassified.map { it.x to it.y }, MaterialTheme.colorScheme.error, 9f)
                    )
                )
                Text("Misclassified points are highlighted with the error color.", style = MaterialTheme.typography.labelSmall)
            }
        }
        item {
            LessonResultCard("Training result", listOf("Misclassified: ${model.mistakes} / ${PerceptronEngine.data.size}", "Weights: (${String.format(Locale.US, "%.2f", model.weightX)}, ${String.format(Locale.US, "%.2f", model.weightY)})", "Bias: ${String.format(Locale.US, "%.2f", model.bias)}"), Modifier.fillMaxWidth())
        }
        item {
            Button(onClick = { trained = !trained }, modifier = Modifier.fillMaxWidth()) { Text(if (trained) "Preview one epoch" else "Train for $epochs epochs") }
        }
    }
}
