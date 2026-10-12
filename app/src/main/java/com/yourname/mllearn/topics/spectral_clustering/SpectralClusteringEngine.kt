package com.yourname.mllearn.topics.spectral_clustering

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import java.util.ArrayDeque

data class SpectralPoint(val x: Float, val y: Float, val cluster: Int)
data class SpectralGraph(val points: List<SpectralPoint>, val edges: List<Pair<Int, Int>>, val clusterCount: Int)

object SpectralClusteringEngine {
    fun buildGraph(clusterCount: Int, neighbors: Int = 2): SpectralGraph {
        val rings = clusterCount.coerceIn(2, 6)
        val pointsPerRing = 20
        val points = buildList {
            repeat(rings) { ring ->
                val radius = 0.12f + ring * (0.34f / (rings - 1))
                repeat(pointsPerRing) { index ->
                    val angle = 2.0 * PI * index / pointsPerRing
                    add(SpectralPoint(0.5f + radius * cos(angle).toFloat(), 0.5f + radius * sin(angle).toFloat(), ring))
                }
            }
        }
        val edges = buildList {
            repeat(rings) { ring ->
                val offset = ring * pointsPerRing
                repeat(pointsPerRing) { index ->
                    for (neighbor in 1..neighbors.coerceIn(1, 5)) {
                        add((offset + index) to (offset + (index + neighbor) % pointsPerRing))
                    }
                }
            }
        }
        val componentLabels = connectedComponents(points.size, edges)
        val labeledPoints = points.mapIndexed { index, point -> point.copy(cluster = componentLabels[index]) }
        return SpectralGraph(labeledPoints, edges, componentLabels.distinct().size)
    }

    private fun connectedComponents(pointCount: Int, edges: List<Pair<Int, Int>>): List<Int> {
        val adjacency = Array(pointCount) { mutableListOf<Int>() }
        edges.forEach { (first, second) ->
            adjacency[first] += second
            adjacency[second] += first
        }
        val labels = IntArray(pointCount) { -1 }
        var nextLabel = 0
        for (start in 0 until pointCount) {
            if (labels[start] != -1) continue
            val queue = ArrayDeque<Int>()
            queue.addLast(start)
            labels[start] = nextLabel
            while (queue.isNotEmpty()) {
                val point = queue.removeFirst()
                adjacency[point].forEach { neighbor ->
                    if (labels[neighbor] == -1) {
                        labels[neighbor] = nextLabel
                        queue.addLast(neighbor)
                    }
                }
            }
            nextLabel++
        }
        return labels.toList()
    }
}
