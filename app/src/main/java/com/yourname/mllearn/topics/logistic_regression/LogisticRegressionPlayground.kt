package com.yourname.mllearn.topics.logistic_regression

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun LogisticRegressionPlayground() {
    var learningRate by remember { mutableFloatStateOf(0.8f) }
    var iterations by remember { mutableIntStateOf(300) }
    var threshold by remember { mutableFloatStateOf(0.5f) }
    val model = remember(learningRate, iterations) { LogisticRegressionEngine.train(learningRate, iterations).last() }
    val boundary = remember(model, threshold) { LogisticRegressionEngine.boundary(model, threshold) }
    val sigmoidCurve = remember(model) {
        (0..80).map { index ->
            val x = index / 80f
            val score = (x - 0.5f) * 12f
            x to LogisticRegressionEngine.sigmoid(score)
        }
    }
    val correct = LogisticRegressionEngine.data.count { point ->
        (if (LogisticRegressionEngine.sigmoid(model.weightX * point.x + model.weightY * point.y + model.bias) >= threshold) 1 else 0) == point.label
    }

    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("Learn a probability boundary", style = MaterialTheme.typography.titleLarge)
            Text("The sigmoid maps a score to a probability. A threshold turns that probability into a class.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            ControlSlider("Learning rate", learningRate, 0.05f..2f, { learningRate = it }, valueLabel = String.format(Locale.US, "%.2f", learningRate))
            ControlSlider("Training updates", iterations.toFloat(), 10f..800f, { iterations = it.toInt() }, steps = 78, valueLabel = iterations.toString())
            ControlSlider("Classification threshold", threshold, 0.1f..0.9f, { threshold = it }, valueLabel = String.format(Locale.US, "%.2f", threshold))
        }
        item {
            LessonSection("Sigmoid") {
                PlotCanvas(lines = listOf(PlotLine(sigmoidCurve, LearnIndigo, 4f)))
                Text("A probability near 0.5 lies at the decision boundary.", style = MaterialTheme.typography.labelSmall)
            }
        }
        item {
            LessonSection("Two-class data") {
                PlotCanvas(
                    lines = listOf(PlotLine(boundary, LearnIndigo, 4f)),
                    scatters = listOf(
                        PlotScatter(LogisticRegressionEngine.data.filter { it.label == 0 }.map { it.x to it.y }, LearnTeal, 6f),
                        PlotScatter(LogisticRegressionEngine.data.filter { it.label == 1 }.map { it.x to it.y }, LearnOrange, 6f)
                    )
                )
                Text("Boundary shifts when training or the probability threshold changes.", style = MaterialTheme.typography.labelSmall)
            }
        }
        item {
            LessonResultCard("Classifier", listOf("Correct examples: $correct / ${LogisticRegressionEngine.data.size}", "Weights: (${String.format(Locale.US, "%.2f", model.weightX)}, ${String.format(Locale.US, "%.2f", model.weightY)})", "Bias: ${String.format(Locale.US, "%.2f", model.bias)}"))
        }
    }
}
