package com.yourname.mllearn.topics.confusion_matrix

import kotlin.math.max

data class ConfusionMetrics(
    val truePositive: Int,
    val falsePositive: Int,
    val trueNegative: Int,
    val falseNegative: Int,
    val accuracy: Float,
    val precision: Float,
    val recall: Float,
    val f1: Float
)

object ConfusionMatrixEngine {
    private val positiveScores = listOf(0.97f, 0.91f, 0.86f, 0.80f, 0.73f, 0.66f, 0.61f, 0.55f, 0.49f, 0.42f)
    private val negativeScores = listOf(0.88f, 0.77f, 0.68f, 0.59f, 0.51f, 0.44f, 0.37f, 0.29f, 0.19f, 0.08f)

    fun evaluate(threshold: Float, classBalance: Float): ConfusionMetrics {
        val positiveCount = (20 * classBalance.coerceIn(0.1f, 0.9f)).toInt().coerceIn(1, 19)
        val negativeCount = 20 - positiveCount
        val positives = List(positiveCount) { positiveScores[it % positiveScores.size] }
        val negatives = List(negativeCount) { negativeScores[it % negativeScores.size] }
        val truePositive = positives.count { it >= threshold }
        val falseNegative = positiveCount - truePositive
        val falsePositive = negatives.count { it >= threshold }
        val trueNegative = negativeCount - falsePositive
        val total = (positiveCount + negativeCount).toFloat()
        val accuracy = (truePositive + trueNegative) / total
        val precision = truePositive.toFloat() / max(1, truePositive + falsePositive)
        val recall = truePositive.toFloat() / max(1, truePositive + falseNegative)
        val f1 = if (precision + recall == 0f) 0f else 2f * precision * recall / (precision + recall)
        return ConfusionMetrics(truePositive, falsePositive, trueNegative, falseNegative, accuracy, precision, recall, f1)
    }

    fun rocCurve(classBalance: Float = 0.5f): List<Pair<Float, Float>> = (0..20).map { index ->
        val threshold = 1f - index / 20f
        val metrics = evaluate(threshold, classBalance)
        val falsePositiveRate = metrics.falsePositive.toFloat() / (metrics.falsePositive + metrics.trueNegative).coerceAtLeast(1)
        falsePositiveRate to metrics.recall
    }
}
