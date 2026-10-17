package com.yourname.mllearn.topics.bias_variance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun BiasVariancePlayground(config: AlgorithmConfig = AlgorithmConfig()) {
    var degree by remember(config) { mutableIntStateOf(config.degree) }
    var noiseAmount by remember(config) { mutableFloatStateOf(config.noise) }
    var noiseEnabled by remember { mutableStateOf(true) }
    val activeNoise = if (noiseEnabled) noiseAmount else 0f
    val data = remember(noiseEnabled, noiseAmount, config.pointCount) { BiasVarianceEngine.data(activeNoise, config.pointCount) }
    val curve = remember(degree) { BiasVarianceEngine.fittedCurve(degree) }
    val trainError = BiasVarianceEngine.trainError(degree, activeNoise)
    val testError = BiasVarianceEngine.testError(degree, activeNoise)

    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("Fit the signal, not the noise", style = MaterialTheme.typography.titleLarge)
            Text("Low degree can miss the curve. High degree bends to every training point and may generalize poorly.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            ControlSlider("Polynomial degree", degree.toFloat(), 1f..15f, { degree = it.toInt() }, steps = 13, valueLabel = degree.toString())
            ControlSlider("Noise amplitude", noiseAmount, 0f..0.12f, { noiseAmount = it }, valueLabel = String.format(Locale.US, "%.2f", noiseAmount))
        }
        item {
            androidx.compose.foundation.layout.Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Switch(checked = noiseEnabled, onCheckedChange = { noiseEnabled = it })
                Text("Add noise to training data", style = MaterialTheme.typography.bodyMedium)
            }
        }
        item {
            LessonSection("Polynomial fit") {
                PlotCanvas(
                    lines = listOf(PlotLine(curve, LearnIndigo, 4f)),
                    scatters = listOf(PlotScatter(data, LearnOrange, 5f))
                )
                Text("Orange points are training samples; the indigo curve is the degree-$degree model.", style = MaterialTheme.typography.labelSmall)
            }
        }
        item {
            LessonResultCard("Generalization", listOf("Training points: ${data.size}", "Training error: ${String.format(Locale.US, "%.3f", trainError)}", "Test error: ${String.format(Locale.US, "%.3f", testError)}", when {
                degree == 1 -> "A straight line is too simple for this curved signal."
                degree >= 12 -> "High flexibility can fit noise and raise test error."
                else -> "This model has enough flexibility to follow the signal."
            }))
        }
    }
}
