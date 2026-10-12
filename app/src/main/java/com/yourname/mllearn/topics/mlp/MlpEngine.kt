package com.yourname.mllearn.topics.mlp

import kotlin.math.exp

data class MlpTrainingResult(val losses: List<Float>, val finalLoss: Float, val hiddenUnits: Int, val epochs: Int)

object MlpEngine {
    private val inputs = arrayOf(floatArrayOf(0f, 0f), floatArrayOf(0f, 1f), floatArrayOf(1f, 0f), floatArrayOf(1f, 1f))
    private val targets = floatArrayOf(0f, 1f, 1f, 0f)

    fun train(learningRate: Float, epochs: Int, hiddenUnits: Int): MlpTrainingResult {
        val hidden = hiddenUnits.coerceIn(2, 8)
        val inputWeights = Array(hidden) { unit -> floatArrayOf((unit - 1) * 0.18f, (1 - unit) * 0.14f) }
        val hiddenBiases = FloatArray(hidden) { unit -> (unit - hidden / 2) * 0.09f }
        val outputWeights = FloatArray(hidden) { unit -> (unit % 2 - 0.5f) * 0.24f }
        var outputBias = 0f
        val losses = mutableListOf(loss(inputWeights, hiddenBiases, outputWeights, outputBias))

        repeat(epochs.coerceIn(1, 5000)) { epoch ->
            inputs.indices.forEach { sample ->
                val activations = FloatArray(hidden) { unit ->
                    sigmoid(inputWeights[unit][0] * inputs[sample][0] + inputWeights[unit][1] * inputs[sample][1] + hiddenBiases[unit])
                }
                val output = sigmoid((0 until hidden).sumOf { (outputWeights[it] * activations[it]).toDouble() }.toFloat() + outputBias)
                val outputDelta = (output - targets[sample]) * output * (1f - output)
                for (unit in 0 until hidden) {
                    val hiddenDelta = outputDelta * outputWeights[unit] * activations[unit] * (1f - activations[unit])
                    outputWeights[unit] -= learningRate * outputDelta * activations[unit]
                    hiddenBiases[unit] -= learningRate * hiddenDelta
                    inputWeights[unit][0] -= learningRate * hiddenDelta * inputs[sample][0]
                    inputWeights[unit][1] -= learningRate * hiddenDelta * inputs[sample][1]
                }
                outputBias -= learningRate * outputDelta
            }
            if ((epoch + 1) % 5 == 0 || epoch == 0) losses += loss(inputWeights, hiddenBiases, outputWeights, outputBias)
        }
        val finalLoss = loss(inputWeights, hiddenBiases, outputWeights, outputBias)
        if (losses.lastOrNull() != finalLoss) losses += finalLoss
        return MlpTrainingResult(losses, finalLoss, hidden, epochs.coerceIn(1, 5000))
    }

    private fun loss(inputWeights: Array<FloatArray>, hiddenBiases: FloatArray, outputWeights: FloatArray, outputBias: Float): Float {
        return inputs.indices.map { sample ->
            val output = sigmoid((outputWeights.indices.sumOf { unit ->
                val hidden = sigmoid(inputWeights[unit][0] * inputs[sample][0] + inputWeights[unit][1] * inputs[sample][1] + hiddenBiases[unit])
                (outputWeights[unit] * hidden).toDouble()
            }.toFloat()) + outputBias)
            val error = output - targets[sample]
            error * error
        }.average().toFloat()
    }

    private fun sigmoid(value: Float): Float = 1f / (1f + exp(-value.coerceIn(-20f, 20f)))
}
