package com.yourname.mllearn.data

data class Topic(
    val id: String,
    val emoji: String,
    val title: String,
    val subtitle: String,
    val available: Boolean = false
)

val ALL_TOPICS = listOf(
    Topic("gradient_descent", "⛰️", "Gradient Descent", "Take small steps toward a lower loss", available = true),
    Topic("loss_functions", "📉", "Loss Functions", "Measure how far predictions miss", available = true),
    Topic("bias_variance", "⚖️", "Bias–Variance Tradeoff", "Balance simplicity and flexibility", available = true),
    Topic("linear_regression", "📈", "Simple Linear Regression", "Fit a line to observed data", available = true),
    Topic("logistic_regression", "🎯", "Logistic Regression", "Predict probabilities for two classes", available = true),
    Topic("kmeans", "🌀", "K-Means Clustering", "Group points around moving centers", available = true),
    Topic("spectral_clustering", "🕸️", "Spectral Clustering", "Find groups with graph structure", available = true),
    Topic("perceptron", "🧠", "Perceptron", "Learn a linear decision boundary", available = true),
    Topic("mlp", "🕸️", "MLP + Backpropagation", "Train a tiny network on XOR", available = true),
    Topic("confusion_matrix", "📊", "Confusion Matrix", "Inspect classification outcomes", available = true)
)
