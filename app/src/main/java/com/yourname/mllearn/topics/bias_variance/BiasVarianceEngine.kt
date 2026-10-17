package com.yourname.mllearn.topics.bias_variance

import kotlin.math.PI
import kotlin.math.sin

object BiasVarianceEngine {
    fun data(noiseLevel: Float, pointCount: Int = 19): List<Pair<Float, Float>> = (0 until pointCount.coerceIn(5, 60)).map { index ->
        val x = index / (pointCount.coerceIn(5, 60) - 1f)
        val clean = signal(x)
        val noise = (((index * 17) % 9) - 4) * noiseLevel.coerceIn(0f, 0.12f)
        x to (clean + noise).coerceIn(0f, 1f)
    }

    fun fittedCurve(degree: Int): List<Pair<Float, Float>> {
        val complexity = degree.coerceIn(1, 15)
        val capacity = (complexity / 5f).coerceIn(0f, 1f)
        val overfit = ((complexity - 5).coerceAtLeast(0) / 10f) * 0.2f
        return (0..100).map { index ->
            val x = index / 100f
            val straight = 0.38f + 0.24f * x
            val flexible = signal(x)
            val oscillation = sin(14.0 * PI * x).toFloat() * overfit
            x to (straight * (1f - capacity) + flexible * capacity + oscillation).coerceIn(0f, 1f)
        }
    }

    fun trainError(degree: Int, noiseLevel: Float): Float =
        (0.26f / degree.coerceAtLeast(1) + noiseLevel.coerceIn(0f, 0.12f) * 0.7f).coerceAtLeast(0.015f)

    fun testError(degree: Int, noiseLevel: Float): Float {
        val underfit = 0.20f / degree.coerceAtLeast(1)
        val overfit = (degree - 5).coerceAtLeast(0) * 0.012f
        return 0.025f + underfit + overfit + noiseLevel.coerceIn(0f, 0.12f) * 0.8f
    }

    private fun signal(x: Float): Float = 0.5f + 0.29f * sin(2.0 * PI * x).toFloat()
}
