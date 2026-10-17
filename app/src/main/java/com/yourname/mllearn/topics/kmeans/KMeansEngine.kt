package com.yourname.mllearn.topics.kmeans

data class ClusterPoint(val x: Float, val y: Float)
data class KMeansState(val centroids: List<ClusterPoint>, val assignments: List<Int>, val iteration: Int)

object KMeansEngine {
    val points = buildList {
        val centers = listOf(ClusterPoint(0.22f, 0.25f), ClusterPoint(0.75f, 0.27f), ClusterPoint(0.50f, 0.76f), ClusterPoint(0.20f, 0.76f), ClusterPoint(0.80f, 0.76f), ClusterPoint(0.50f, 0.25f))
        centers.forEachIndexed { group, center ->
            repeat(8) { index ->
                val dx = (((index * 7 + group * 3) % 9) - 4) * 0.018f
                val dy = (((index * 5 + group * 2) % 9) - 4) * 0.017f
                add(ClusterPoint((center.x + dx).coerceIn(0.04f, 0.96f), (center.y + dy).coerceIn(0.04f, 0.96f)))
            }
        }
    }

    fun initial(k: Int, seed: Int = 0): KMeansState {
        val seeds = listOf(ClusterPoint(0.25f, 0.25f), ClusterPoint(0.75f, 0.75f), ClusterPoint(0.25f, 0.75f), ClusterPoint(0.75f, 0.25f), ClusterPoint(0.5f, 0.5f), ClusterPoint(0.5f, 0.15f))
        val shift = seed.mod(seeds.size)
        val orderedSeeds = seeds.drop(shift) + seeds.take(shift)
        val centroids = orderedSeeds.take(k.coerceIn(2, 6))
        return KMeansState(centroids, assign(points, centroids), 0)
    }

    fun step(state: KMeansState): KMeansState {
        val moved = state.centroids.indices.map { cluster ->
            val members = points.filterIndexed { index, _ -> state.assignments[index] == cluster }
            if (members.isEmpty()) state.centroids[cluster] else ClusterPoint(members.map { it.x }.average().toFloat(), members.map { it.y }.average().toFloat())
        }
        return KMeansState(moved, assign(points, moved), state.iteration + 1)
    }

    private fun assign(points: List<ClusterPoint>, centroids: List<ClusterPoint>): List<Int> = points.map { point ->
        centroids.indices.minByOrNull { cluster ->
            val dx = point.x - centroids[cluster].x
            val dy = point.y - centroids[cluster].y
            dx * dx + dy * dy
        } ?: 0
    }
}
