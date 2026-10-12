package com.yourname.mllearn.topics.confusion_matrix

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yourname.mllearn.ui.components.ControlSlider
import com.yourname.mllearn.ui.components.LessonResultCard
import com.yourname.mllearn.ui.components.LessonSection
import com.yourname.mllearn.ui.components.PlotCanvas
import com.yourname.mllearn.ui.components.PlotLine
import com.yourname.mllearn.ui.theme.LearnIndigo
import java.util.Locale

@Composable
fun ConfusionMatrixPlayground() {
    var threshold by remember { mutableFloatStateOf(0.5f) }
    var balance by remember { mutableFloatStateOf(0.5f) }
    var showRoc by remember { mutableStateOf(false) }
    val metrics = remember(threshold, balance) { ConfusionMatrixEngine.evaluate(threshold, balance) }
    val percentPositive = (balance * 100).toInt()

    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("Look beyond accuracy", style = MaterialTheme.typography.titleLarge)
            Text("Move the score threshold and class balance to see how each kind of prediction changes.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            ControlSlider("Positive threshold", threshold, 0f..1f, { threshold = it }, valueLabel = String.format(Locale.US, "%.2f", threshold))
            ControlSlider("Positive class balance", balance, 0.1f..0.9f, { balance = it }, valueLabel = "$percentPositive%")
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(!showRoc, onClick = { showRoc = false }, label = { Text("Matrix") })
                FilterChip(showRoc, onClick = { showRoc = true }, label = { Text("ROC curve") })
            }
        }
        item {
            if (showRoc) {
                LessonSection("Threshold tradeoff") {
                    PlotCanvas(lines = listOf(PlotLine(ConfusionMatrixEngine.rocCurve(), LearnIndigo, 4f)))
                    Text("Each point corresponds to a different threshold; farther right means more positive cases are found.", style = MaterialTheme.typography.labelSmall)
                }
            } else {
                LessonSection("Predicted vs actual") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        MatrixCell("Predicted +", "TP  ${metrics.truePositive}", "FP  ${metrics.falsePositive}", MaterialTheme.colorScheme.primaryContainer, Modifier.weight(1f))
                        MatrixCell("Predicted −", "FN  ${metrics.falseNegative}", "TN  ${metrics.trueNegative}", MaterialTheme.colorScheme.secondaryContainer, Modifier.weight(1f))
                    }
                    Text("Columns are predictions; each cell names the actual class outcome.", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        item {
            LessonResultCard("Classifier metrics", listOf("Accuracy: ${String.format(Locale.US, "%.2f", metrics.accuracy)}", "Precision: ${String.format(Locale.US, "%.2f", metrics.precision)}", "Recall: ${String.format(Locale.US, "%.2f", metrics.recall)}", "F1: ${String.format(Locale.US, "%.2f", metrics.f1)}"))
        }
    }
}

@Composable
private fun MatrixCell(title: String, first: String, second: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = color, shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            Text(first, style = MaterialTheme.typography.titleMedium)
            Text(second, style = MaterialTheme.typography.titleMedium)
        }
    }
}
