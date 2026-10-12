package com.yourname.mllearn.topics.gradient_descent

enum class DescentVariant(val label: String) {
    BATCH("Batch"),
    SGD("SGD"),
    MINI_BATCH("Mini-batch")
}

data class DescentStep(val weight: Float, val loss: Float)

object GradientDescentEngine {
    const val PLOT_MIN_WEIGHT = -14f
    const val PLOT_MAX_WEIGHT = 14f
    private const val PLOT_MAX_LOSS = 324f

    fun loss(weight: Float, target: Float): Float {
        val error = weight - target
        return error * error
    }

    fun gradient(weight: Float, target: Float): Float = 2f * (weight - target)

    fun optimize(
        startWeight: Float,
        target: Float,
        learningRate: Float,
        iterations: Int,
        variant: DescentVariant
    ): List<DescentStep> {
        val path = mutableListOf(DescentStep(startWeight, loss(startWeight, target)))
        var weight = startWeight
        repeat(iterations.coerceAtLeast(0)) {
            // With one example, every batch variant receives this same exact gradient.
            val slope = when (variant) {
                DescentVariant.BATCH, DescentVariant.SGD, DescentVariant.MINI_BATCH -> gradient(weight, target)
            }
            weight -= learningRate * slope
            path += DescentStep(weight, loss(weight, target))
        }
        return path
    }

    fun normalizedCurve(target: Float, samples: Int = 121): List<Pair<Float, Float>> =
        (0..samples).map { index ->
            val weight = PLOT_MIN_WEIGHT + (PLOT_MAX_WEIGHT - PLOT_MIN_WEIGHT) * index / samples
            normalize(weight, target)
        }

    fun normalizedPath(path: List<DescentStep>, target: Float): List<Pair<Float, Float>> =
        path.map { normalize(it.weight, target) }

    private fun normalize(weight: Float, target: Float): Pair<Float, Float> {
        val x = (weight - PLOT_MIN_WEIGHT) / (PLOT_MAX_WEIGHT - PLOT_MIN_WEIGHT)
        val y = (loss(weight, target) / PLOT_MAX_LOSS).coerceIn(0f, 1f)
        return x.coerceIn(0f, 1f) to y
    }
}
