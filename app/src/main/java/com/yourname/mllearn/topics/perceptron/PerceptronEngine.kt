package com.yourname.mllearn.topics.perceptron

data class PerceptronPoint(val x: Float, val y: Float, val label: Int)
data class PerceptronModel(val weightX: Float, val weightY: Float, val bias: Float, val mistakes: Int)

object PerceptronEngine {
    val data = buildList {
        for (index in 0..9) {
            val t = index / 9f
            add(PerceptronPoint(0.08f + 0.36f * t, 0.08f + 0.30f * t, -1))
            add(PerceptronPoint(0.56f + 0.36f * t, 0.58f + 0.30f * t, 1))
        }
    }

    fun train(learningRate: Float, epochs: Int): PerceptronModel {
        var weightX = 0f
        var weightY = 0f
        var bias = 0f
        repeat(epochs.coerceIn(1, 100)) {
            data.forEach { point ->
                val prediction = if (weightX * point.x + weightY * point.y + bias >= 0f) 1 else -1
                if (prediction != point.label) {
                    weightX += learningRate * point.label * point.x
                    weightY += learningRate * point.label * point.y
                    bias += learningRate * point.label
                }
            }
        }
        val mistakes = data.count { point -> (if (weightX * point.x + weightY * point.y + bias >= 0f) 1 else -1) != point.label }
        return PerceptronModel(weightX, weightY, bias, mistakes)
    }

    fun boundary(model: PerceptronModel): List<Pair<Float, Float>> = (0..50).map { index ->
        val x = index / 50f
        val y = if (kotlin.math.abs(model.weightY) < 0.001f) 0.5f
        else (-(model.weightX * x + model.bias) / model.weightY).coerceIn(0f, 1f)
        x to y
    }
}
