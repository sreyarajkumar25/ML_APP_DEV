package com.yourname.mllearn.topics.kmeans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yourname.mllearn.ui.components.ClusterPalette
import com.yourname.mllearn.ui.components.ControlSlider
import com.yourname.mllearn.ui.components.LessonResultCard
import com.yourname.mllearn.ui.components.LessonSection
import com.yourname.mllearn.ui.components.PlotCanvas
import com.yourname.mllearn.ui.components.PlotScatter

@Composable
fun KMeansPlayground() {
    var k by remember { mutableIntStateOf(3) }
    var runIterations by remember { mutableIntStateOf(5) }
    var state by remember(k) { mutableStateOf(KMeansEngine.initial(k)) }

    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("Watch the centers move", style = MaterialTheme.typography.titleLarge)
            Text("Each step assigns points to their nearest centroid, then moves each centroid to its group's mean.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            ControlSlider("Number of clusters K", k.toFloat(), 2f..6f, { k = it.toInt() }, steps = 3, valueLabel = k.toString())
            ControlSlider("Steps per run", runIterations.toFloat(), 1f..12f, { runIterations = it.toInt() }, steps = 10, valueLabel = runIterations.toString())
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { state = KMeansEngine.step(state) }) { Text("Step") }
                    Button(onClick = { repeat(runIterations) { state = KMeansEngine.step(state) } }) { Text("Run $runIterations steps") }
                }
                Button(onClick = { state = KMeansEngine.initial(k) }) { Text("Reset") }
            }
        }
        item {
            LessonSection("Cluster assignments") {
                val scatters = (0 until k).map { cluster ->
                    PlotScatter(KMeansEngine.points.filterIndexed { index, _ -> state.assignments[index] == cluster }.map { it.x to it.y }, ClusterPalette[cluster], 6f)
                } + state.centroids.mapIndexed { cluster, center -> PlotScatter(listOf(center.x to center.y), ClusterPalette[cluster], 10f) }
                PlotCanvas(lines = emptyList(), scatters = scatters)
                Text("Colored dots are data; larger dots are centroids.", style = MaterialTheme.typography.labelSmall)
            }
        }
        item {
            LessonResultCard("Iteration ${state.iteration}", state.centroids.mapIndexed { index, center -> "Center ${index + 1}: (${"%.2f".format(center.x)}, ${"%.2f".format(center.y)})" })
        }
    }
}
