package com.yourname.mllearn.topics.linear_regression

import kotlin.math.abs

data class RegressionPoint(val x: Float, val y: Float)
data class RegressionModel(val weight: Float, val bias: Float, val loss: Float)

object LinearRegressionEngine {
    val data = listOf(
        RegressionPoint(0.05f, 0.17f), RegressionPoint(0.16f, 0.32f),
        RegressionPoint(0.27f, 0.39f), RegressionPoint(0.39f, 0.48f),
        RegressionPoint(0.51f, 0.58f), RegressionPoint(0.62f, 0.61f),
        RegressionPoint(0.74f, 0.78f), RegressionPoint(0.86f, 0.81f),
        RegressionPoint(0.96f, 0.95f)
    )

    fun fit(learningRate: Float, iterations: Int): List<RegressionModel> {
        var weight = 0f
        var bias = 0f
        val history = mutableListOf(model(weight, bias))
        repeat(iterations.coerceIn(1, 1000)) {
            var weightGradient = 0f
            var biasGradient = 0f
            data.forEach { point ->
                val error = weight * point.x + bias - point.y
                weightGradient += error * point.x
                biasGradient += error
            }
            val scale = 2f / data.size
            weight -= learningRate * weightGradient * scale
            bias -= learningRate * biasGradient * scale
            history += model(weight, bias)
        }
        return history
    }

    fun line(model: RegressionModel): List<Pair<Float, Float>> = (0..60).map { index ->
        val x = index / 60f
        x to (model.weight * x + model.bias).coerceIn(0f, 1f)
    }

    private fun model(weight: Float, bias: Float): RegressionModel {
        val loss = data.sumOf { point ->
            val error = weight * point.x + bias - point.y
            (error * error).toDouble()
        }.toFloat() / data.size
        return RegressionModel(weight, bias, loss)
    }
}
