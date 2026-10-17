package com.yourname.mllearn.topics.linear_regression

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
import com.yourname.mllearn.data.AlgorithmConfig
import com.yourname.mllearn.ui.components.ControlSlider
import com.yourname.mllearn.ui.components.LessonResultCard
import com.yourname.mllearn.ui.components.LessonSection
import com.yourname.mllearn.ui.components.PlotCanvas
import com.yourname.mllearn.ui.components.PlotLine
import com.yourname.mllearn.ui.components.PlotScatter
import com.yourname.mllearn.ui.theme.LearnIndigo
import com.yourname.mllearn.ui.theme.LearnOrange
import java.util.Locale

@Composable
fun LinearRegressionPlayground(config: AlgorithmConfig = AlgorithmConfig()) {
    var learningRate by remember(config) { mutableFloatStateOf(config.learningRate) }
    var iterations by remember(config) { mutableIntStateOf(config.iterations) }
    var showFit by remember { mutableStateOf(true) }
    val history = remember(learningRate, iterations, config.startWeight, config.startBias) {
        LinearRegressionEngine.fit(learningRate, iterations, config.startWeight, config.startBias)
    }
    val model = if (showFit) history.last() else LinearRegressionEngine.fit(0f, 1, config.startWeight, config.startBias).first()

    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("Fit y = wx + b", style = MaterialTheme.typography.titleLarge)
            Text("Move the learning rate and iteration count; the line is refit from the same example points.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            ControlSlider("Learning rate", learningRate, 0.01f..0.8f, { learningRate = it }, valueLabel = String.format(Locale.US, "%.2f", learningRate))
            ControlSlider("Iterations", iterations.toFloat(), 1f..500f, { iterations = it.toInt() }, steps = 498, valueLabel = iterations.toString())
        }
        item {
            FilterChip(showFit, onClick = { showFit = !showFit }, label = { Text(if (showFit) "Fitted line" else "Show fitted line") })
        }
        item {
            LessonSection("Observed data and model") {
                PlotCanvas(
                    lines = listOf(PlotLine(LinearRegressionEngine.line(model), LearnIndigo, 4f)),
                    scatters = listOf(PlotScatter(LinearRegressionEngine.data.map { it.x to it.y }, LearnOrange, 6f))
                )
                Text("Orange: observations    Indigo: current prediction", style = MaterialTheme.typography.labelSmall)
            }
        }
        item {
            LessonResultCard("Fitted parameters", listOf("Slope w: ${String.format(Locale.US, "%.3f", model.weight)}", "Intercept b: ${String.format(Locale.US, "%.3f", model.bias)}", "Mean squared error: ${String.format(Locale.US, "%.4f", model.loss)}"), Modifier.fillMaxWidth())
        }
        item {
            Button(onClick = { showFit = false }, modifier = Modifier.fillMaxWidth()) { Text("Reset to starting line") }
        }
    }
}
