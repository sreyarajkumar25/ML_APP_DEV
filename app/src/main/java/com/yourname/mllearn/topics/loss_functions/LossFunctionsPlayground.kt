package com.yourname.mllearn.topics.loss_functions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.yourname.mllearn.ui.theme.LearnIndigo
import com.yourname.mllearn.ui.theme.LearnOrange
import com.yourname.mllearn.ui.theme.LearnTeal
import java.util.Locale

@Composable
fun LossFunctionsPlayground() {
    var error by remember { mutableFloatStateOf(0.45f) }
    var sampleWeight by remember { mutableFloatStateOf(1f) }
    var selected by remember { mutableStateOf("MSE") }
    val curves = remember { LossFunctionsEngine.curves() }
    val currentLoss = when (selected) {
        "MAE" -> LossFunctionsEngine.mae(error)
        "Cross-entropy" -> LossFunctionsEngine.crossEntropy(error)
        else -> LossFunctionsEngine.mse(error)
    } * sampleWeight

    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("One error, three penalties", style = MaterialTheme.typography.titleLarge)
            Text("Compare how each objective reacts as a prediction moves away from its target.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("MSE", "MAE", "Cross-entropy").forEach { kind ->
                    FilterChip(selected == kind, onClick = { selected = kind }, label = { Text(kind) })
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                ControlSlider("Prediction error", error, 0f..0.95f, { error = it }, valueLabel = String.format(Locale.US, "%.2f", error))
                ControlSlider("Sample weight", sampleWeight, 0.25f..2f, { sampleWeight = it }, valueLabel = String.format(Locale.US, "%.2f×", sampleWeight))
            }
        }
        item {
            LessonSection("Loss curves") {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(Triple("MSE", curves.getValue("MSE"), LearnIndigo), Triple("MAE", curves.getValue("MAE"), LearnTeal), Triple("CE", curves.getValue("Cross-entropy"), LearnOrange)).forEach { (label, curve, color) ->
                        Column(modifier = Modifier.weight(1f)) {
                            Text(label, style = MaterialTheme.typography.labelMedium)
                            val weightedCurve = curve.map { (x, y) -> x to (1f - ((1f - y) * sampleWeight).coerceIn(0f, 1f)) }
                            PlotCanvas(lines = listOf(PlotLine(weightedCurve, color, 3f)), plotHeight = 130.dp)
                        }
                    }
                }
                Text("Horizontal: error 0 → 1     Higher curves mean larger loss", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item {
            LessonResultCard("$selected at this error", listOf("Loss: ${String.format(Locale.US, "%.4f", currentLoss)}", "A larger sample weight scales the penalty without changing the error."))
        }
    }
}
