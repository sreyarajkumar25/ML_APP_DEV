package com.yourname.mllearn.topics.logistic_regression

import kotlin.math.exp

data class LogisticPoint(val x: Float, val y: Float, val label: Int)
data class LogisticModel(val weightX: Float, val weightY: Float, val bias: Float, val loss: Float)

object LogisticRegressionEngine {
    val data = buildList {
        for (index in 0..11) {
            val offset = index / 11f
            add(LogisticPoint(0.12f + 0.27f * offset, 0.12f + 0.27f * offset, 0))
            add(LogisticPoint(0.61f + 0.27f * offset, 0.61f + 0.27f * offset, 1))
        }
    }

    fun sigmoid(value: Float): Float = (1f / (1f + exp(-value.coerceIn(-20f, 20f))))

    fun train(learningRate: Float, iterations: Int): List<LogisticModel> {
        var weightX = 0f
        var weightY = 0f
        var bias = 0f
        val history = mutableListOf(model(weightX, weightY, bias))
        repeat(iterations.coerceIn(1, 1000)) {
            var gradientX = 0f
            var gradientY = 0f
            var gradientBias = 0f
            data.forEach { point ->
                val error = sigmoid(weightX * point.x + weightY * point.y + bias) - point.label
                gradientX += error * point.x
                gradientY += error * point.y
                gradientBias += error
            }
            val scale = learningRate / data.size
            weightX -= scale * gradientX
            weightY -= scale * gradientY
            bias -= scale * gradientBias
            history += model(weightX, weightY, bias)
        }
        return history
    }

    fun boundary(model: LogisticModel, threshold: Float = 0.5f): List<Pair<Float, Float>> = (0..50).map { index ->
        val x = index / 50f
        val logit = kotlin.math.ln(threshold.coerceIn(0.001f, 0.999f) / (1f - threshold.coerceIn(0.001f, 0.999f)))
        val y = if (kotlin.math.abs(model.weightY) < 0.001f) 0.5f
        else ((logit - model.weightX * x - model.bias) / model.weightY).coerceIn(0f, 1f)
        x to y
    }

    private fun model(weightX: Float, weightY: Float, bias: Float): LogisticModel {
        val loss = data.sumOf { point ->
            val probability = sigmoid(weightX * point.x + weightY * point.y + bias).coerceIn(0.0001f, 0.9999f)
            val pointLoss = if (point.label == 1) -kotlin.math.ln(probability) else -kotlin.math.ln(1f - probability)
            pointLoss.toDouble()
        }.toFloat() / data.size
        return LogisticModel(weightX, weightY, bias, loss)
    }
}
