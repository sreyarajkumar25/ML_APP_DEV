package com.yourname.mllearn.topics.mlp

import kotlin.math.exp

data class MlpTrainingResult(val losses: List<Float>, val finalLoss: Float, val hiddenUnits: Int, val epochs: Int, val hiddenLayers: Int)

object MlpEngine {
    private val inputs = arrayOf(floatArrayOf(0f, 0f), floatArrayOf(0f, 1f), floatArrayOf(1f, 0f), floatArrayOf(1f, 1f))
    private val targets = floatArrayOf(0f, 1f, 1f, 0f)

    fun train(learningRate: Float, epochs: Int, hiddenUnits: Int, hiddenLayers: Int = 1): MlpTrainingResult {
        val hidden = hiddenUnits.coerceIn(2, 8)
        val depth = hiddenLayers.coerceIn(1, 3)
        val widths = listOf(2) + List(depth) { hidden } + 1
        val weights = Array(widths.lastIndex) { layer ->
            Array(widths[layer + 1]) { destination ->
                FloatArray(widths[layer]) { source ->
                    (((destination * 13 + source * 7 + layer * 5) % 11) - 5) * 0.06f
                }
            }
        }
        val biases = Array(widths.lastIndex) { layer ->
            FloatArray(widths[layer + 1]) { unit -> ((unit + layer) % 5 - 2) * 0.04f }
        }
        val losses = mutableListOf(loss(weights, biases))

        val epochCount = epochs.coerceIn(1, 5000)
        repeat(epochCount) { epoch ->
            inputs.indices.forEach { sample ->
                val activations = mutableListOf(inputs[sample])
                weights.indices.forEach { layer ->
                    val previous = activations.last()
                    activations += FloatArray(weights[layer].size) { destination ->
                        sigmoid(weights[layer][destination].indices.sumOf { source ->
                            (weights[layer][destination][source] * previous[source]).toDouble()
                        }.toFloat() + biases[layer][destination])
                    }
                }

                val deltas = Array(weights.size) { layer -> FloatArray(weights[layer].size) }
                val outputLayer = weights.lastIndex
                val output = activations.last()[0]
                deltas[outputLayer][0] = (output - targets[sample]) * output * (1f - output)
                for (layer in outputLayer - 1 downTo 0) {
                    for (unit in deltas[layer].indices) {
                        val activation = activations[layer + 1][unit]
                        val downstream = deltas[layer + 1].indices.sumOf { next ->
                            (weights[layer + 1][next][unit] * deltas[layer + 1][next]).toDouble()
                        }.toFloat()
                        deltas[layer][unit] = downstream * activation * (1f - activation)
                    }
                }
                weights.indices.forEach { layer ->
                    for (destination in weights[layer].indices) {
                        for (source in weights[layer][destination].indices) {
                            weights[layer][destination][source] -= learningRate * deltas[layer][destination] * activations[layer][source]
                        }
                        biases[layer][destination] -= learningRate * deltas[layer][destination]
                    }
                }
            }
            if ((epoch + 1) % 5 == 0 || epoch == 0) losses += loss(weights, biases)
        }
        val finalLoss = loss(weights, biases)
        if (losses.lastOrNull() != finalLoss) losses += finalLoss
        return MlpTrainingResult(losses, finalLoss, hidden, epochCount, depth)
    }

    private fun loss(weights: Array<Array<FloatArray>>, biases: Array<FloatArray>): Float {
        return inputs.indices.map { sample ->
            var activations = inputs[sample]
            weights.indices.forEach { layer ->
                activations = FloatArray(weights[layer].size) { destination ->
                    sigmoid(weights[layer][destination].indices.sumOf { source ->
                        (weights[layer][destination][source] * activations[source]).toDouble()
                    }.toFloat() + biases[layer][destination])
                }
            }
            val output = activations[0]
            val error = output - targets[sample]
            error * error
        }.average().toFloat()
    }

    private fun sigmoid(value: Float): Float = 1f / (1f + exp(-value.coerceIn(-20f, 20f)))
}
