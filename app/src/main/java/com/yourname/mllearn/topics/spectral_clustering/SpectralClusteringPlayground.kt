package com.yourname.mllearn.topics.spectral_clustering

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yourname.mllearn.ui.components.ClusterPalette
import com.yourname.mllearn.ui.components.ControlSlider
import com.yourname.mllearn.ui.components.LessonResultCard
import com.yourname.mllearn.ui.components.LessonSection
import com.yourname.mllearn.ui.components.PlotCanvas
import com.yourname.mllearn.ui.components.PlotLine
import com.yourname.mllearn.ui.components.PlotScatter

@Composable
fun SpectralClusteringPlayground() {
    var clusterCount by remember { mutableIntStateOf(2) }
    var neighbors by remember { mutableIntStateOf(2) }
    var showGraph by remember { mutableStateOf(true) }
    val graph = remember(clusterCount, neighbors) { SpectralClusteringEngine.buildGraph(clusterCount, neighbors) }
    val edgeLines = remember(graph, showGraph) {
        if (!showGraph) emptyList() else graph.edges.map { (first, second) ->
            val start = graph.points[first]
            val end = graph.points[second]
            PlotLine(listOf(start.x to start.y, end.x to end.y), Color(0xFF9E9E9E).copy(alpha = 0.38f), 1.2f)
        }
    }

    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("A graph separates the rings", style = MaterialTheme.typography.titleLarge)
            Text("Nearby points become graph neighbors. Here each ring is a connected component; Laplacian eigenmaps expose those components before assigning cluster colors.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            ControlSlider("Number of rings / clusters", clusterCount.toFloat(), 2f..6f, { clusterCount = it.toInt() }, steps = 3, valueLabel = clusterCount.toString())
            ControlSlider("Neighbors per point", neighbors.toFloat(), 1f..5f, { neighbors = it.toInt() }, steps = 3, valueLabel = neighbors.toString())
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(showGraph, onClick = { showGraph = !showGraph }, label = { Text(if (showGraph) "Graph visible" else "Show graph") })
            }
        }
        item {
            LessonSection("Concentric circles") {
                val pointGroups = (0 until graph.clusterCount).map { cluster ->
                    PlotScatter(graph.points.filter { it.cluster == cluster }.map { it.x to it.y }, ClusterPalette[cluster], 5f)
                }
                PlotCanvas(lines = edgeLines, scatters = pointGroups)
                Text("Gray links show graph adjacency; point colors show component assignment.", style = MaterialTheme.typography.labelSmall)
            }
        }
        item {
            LessonResultCard("Graph embedding", listOf("Nodes: ${graph.points.size}", "Edges: ${graph.edges.size}", "Connected ring groups: ${graph.clusterCount}", "A full spectral pipeline clusters eigenvectors of the graph Laplacian."))
        }
    }
}
