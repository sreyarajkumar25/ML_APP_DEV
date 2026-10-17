package com.yourname.mllearn.data

data class AlgorithmConfig(
    val learningRate: Float = 0.2f,
    val iterations: Int = 100,
    val startWeight: Float = 0f,
    val startBias: Float = 0f,
    val target: Float = 2f,
    val threshold: Float = 0.5f,
    val classBalance: Float = 0.5f,
    val degree: Int = 5,
    val noise: Float = 0.04f,
    val pointCount: Int = 19,
    val lossType: String = "MSE",
    val sampleWeight: Float = 1f,
    val clusters: Int = 3,
    val neighbors: Int = 2,
    val clusterIterations: Int = 5,
    val randomSeed: Int = 0,
    val batchSize: Int = 4,
    val variant: String = "BATCH",
    val hiddenLayers: Int = 1,
    val neurons: Int = 4
) {
    fun toRouteValue(): String = listOf(
        learningRate, iterations, startWeight, startBias, target, threshold, classBalance,
        degree, noise, pointCount, lossType, sampleWeight, clusters, neighbors,
        clusterIterations, randomSeed, batchSize, variant, hiddenLayers, neurons
    ).joinToString("~")

    companion object {
        fun forTopic(topicId: String) = when (topicId) {
            "gradient_descent" -> AlgorithmConfig(learningRate = 0.15f, iterations = 12, startWeight = -4f)
            "loss_functions" -> AlgorithmConfig(target = 0.45f, sampleWeight = 1f)
            "bias_variance" -> AlgorithmConfig(degree = 5, noise = 0.04f)
            "linear_regression" -> AlgorithmConfig(learningRate = 0.3f, iterations = 100)
            "logistic_regression" -> AlgorithmConfig(learningRate = 0.8f, iterations = 300)
            "kmeans" -> AlgorithmConfig(clusters = 3, clusterIterations = 5)
            "spectral_clustering" -> AlgorithmConfig(clusters = 2, neighbors = 2)
            "perceptron" -> AlgorithmConfig(learningRate = 0.2f, iterations = 8)
            "mlp" -> AlgorithmConfig(learningRate = 0.8f, iterations = 300, neurons = 4)
            "confusion_matrix" -> AlgorithmConfig(threshold = 0.5f, classBalance = 0.5f)
            else -> AlgorithmConfig()
        }

        fun fromRouteValue(value: String?): AlgorithmConfig {
            val fields = value?.split('~') ?: return AlgorithmConfig()
            if (fields.size != 20) return AlgorithmConfig()
            return runCatching {
                AlgorithmConfig(
                    learningRate = fields[0].toFloat(), iterations = fields[1].toInt(),
                    startWeight = fields[2].toFloat(), startBias = fields[3].toFloat(),
                    target = fields[4].toFloat(), threshold = fields[5].toFloat(),
                    classBalance = fields[6].toFloat(), degree = fields[7].toInt(),
                    noise = fields[8].toFloat(), pointCount = fields[9].toInt(),
                    lossType = fields[10], sampleWeight = fields[11].toFloat(),
                    clusters = fields[12].toInt(), neighbors = fields[13].toInt(),
                    clusterIterations = fields[14].toInt(), randomSeed = fields[15].toInt(),
                    batchSize = fields[16].toInt(), variant = fields[17],
                    hiddenLayers = fields[18].toInt(), neurons = fields[19].toInt()
                ).validated()
            }.getOrDefault(AlgorithmConfig())
        }
    }

    fun validated() = copy(
        learningRate = learningRate.coerceIn(0.01f, 2f),
        iterations = iterations.coerceIn(1, 1000),
        startWeight = startWeight.coerceIn(-5f, 5f),
        startBias = startBias.coerceIn(-5f, 5f),
        target = target.coerceIn(-4f, 4f),
        threshold = threshold.coerceIn(0.1f, 0.9f),
        classBalance = classBalance.coerceIn(0.1f, 0.9f),
        degree = degree.coerceIn(1, 15), noise = noise.coerceIn(0f, 0.12f),
        pointCount = pointCount.coerceIn(5, 60),
        lossType = lossType.takeIf { it in listOf("MSE", "MAE", "Cross-entropy") } ?: "MSE",
        sampleWeight = sampleWeight.coerceIn(0.25f, 2f),
        clusters = clusters.coerceIn(2, 6), neighbors = neighbors.coerceIn(1, 5),
        clusterIterations = clusterIterations.coerceIn(1, 12),
        randomSeed = randomSeed.coerceIn(0, 99), batchSize = batchSize.coerceIn(1, 32),
        variant = variant.takeIf { it in listOf("BATCH", "SGD", "MINI_BATCH") } ?: "BATCH",
        hiddenLayers = hiddenLayers.coerceIn(1, 3), neurons = neurons.coerceIn(2, 8)
    )

}