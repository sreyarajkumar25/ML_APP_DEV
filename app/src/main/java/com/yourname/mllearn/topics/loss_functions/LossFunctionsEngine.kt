package com.yourname.mllearn.topics.loss_functions

import kotlin.math.ln
import kotlin.math.max

object LossFunctionsEngine {
    fun mse(error: Float): Float = error * error
    fun mae(error: Float): Float = kotlin.math.abs(error)

    fun crossEntropy(error: Float): Float {
        val probability = (1f - error.coerceIn(0f, 0.999f)).coerceIn(0.001f, 1f)
        return -ln(probability)
    }

    fun curves(): Map<String, List<Pair<Float, Float>>> {
        val errors = (0..60).map { it / 60f }
        val maxCrossEntropy = crossEntropy(0.99f)
        return mapOf(
            "MSE" to errors.map { it to 1f - mse(it) },
            "MAE" to errors.map { it to 1f - mae(it) },
            "Cross-entropy" to errors.map { it to 1f - (crossEntropy(it) / max(1f, maxCrossEntropy)).coerceIn(0f, 1f) }
        )
    }
}
