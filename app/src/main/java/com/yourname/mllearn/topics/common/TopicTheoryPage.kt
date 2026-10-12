package com.yourname.mllearn.topics.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yourname.mllearn.ui.components.FormulaView

private data class TheorySpec(
    val heading: String,
    val intro: String,
    val formula: String,
    val variants: List<String>,
    val uses: String
)

private val theorySpecs = mapOf(
    "loss_functions" to TheorySpec("A score for being wrong", "A loss function turns prediction mistakes into a number an optimizer can reduce. Different losses make different tradeoffs: some penalize large errors sharply, while others are more robust.", "MSE = (y - ŷ)²\nMAE = |y - ŷ|\nCE = -[y log(p) + (1-y) log(1-p)]", listOf("MSE strongly penalizes large errors.", "MAE grows linearly and is less sensitive to outliers.", "Cross-entropy penalizes confident, incorrect class probabilities."), "Regression, binary classification, and model training objectives."),
    "bias_variance" to TheorySpec("Model complexity has a cost", "A simple model may miss real structure (high bias). A very flexible model can memorize training noise (high variance). The best model generalizes to data it has not seen.", "Expected test error ≈ bias² + variance + noise", listOf("Degree 1: often underfits curved patterns.", "Moderate degree: can capture the signal.", "Very high degree: may fit noise and generalize poorly."), "Choosing model capacity, regularization strength, and training duration."),
    "linear_regression" to TheorySpec("Fit a line to examples", "Linear regression predicts a continuous value from a weighted input. Training adjusts the slope and intercept to make predictions close to observed targets.", "ŷ = wx + b\nMSE = (1/n) Σ(ŷᵢ - yᵢ)²", listOf("One feature gives a line; multiple features give a hyperplane.", "Gradient descent updates parameters using the loss gradient.", "The fitted line summarizes a linear trend, not necessarily causation."), "Forecasting measurements, estimating trends, and building simple baselines."),
    "logistic_regression" to TheorySpec("Turn a score into a probability", "Logistic regression applies a sigmoid to a weighted sum. The result is a probability between zero and one; a threshold turns it into a class prediction.", "p(y=1|x) = σ(w·x + b)\nσ(z) = 1 / (1 + e⁻ᶻ)", listOf("A threshold of 0.5 is common, but can be moved.", "Cross-entropy is the standard binary training loss.", "The boundary is linear in the input features."), "Interpretable binary classification and probability estimates."),
    "kmeans" to TheorySpec("Let nearby points share a center", "K-means alternates between assigning every point to its nearest centroid and moving each centroid to the mean of its assigned points. Repeating these steps reduces within-cluster squared distance.", "Assignment: cᵢ = argminₖ ||xᵢ - μₖ||²\nUpdate: μₖ = mean(xᵢ assigned to k)", listOf("K is chosen before fitting.", "Different initial centroids can produce different clusters.", "Clusters are represented by their moving centroids."), "Exploring unlabeled data, grouping customers, and compressing colors."),
    "spectral_clustering" to TheorySpec("Use a graph to reveal groups", "Spectral clustering connects nearby data points, builds a graph Laplacian, and embeds the graph using eigenvectors. Points that are hard to separate in the original space may form clear groups in that embedding.", "L = D - A\nLₙ = I - D⁻¹ᐟ² A D⁻¹ᐟ²", listOf("A records edge strengths between nearby points.", "D records each node's total edge weight.", "Laplacian eigenmaps expose graph connectivity before clustering."), "Non-convex shapes such as rings or moons that centroid methods struggle with."),
    "perceptron" to TheorySpec("A first learnable decision boundary", "A perceptron predicts a class from the sign of a weighted sum. Whenever it makes a mistake, it nudges its weights toward the correct class.", "ŷ = sign(w·x + b)\nw ← w + η y x  (on a mistake)", listOf("A single perceptron learns only a linear boundary.", "Learning rate controls each correction.", "It converges when the training data is linearly separable."), "A compact introduction to online learning and linear classifiers."),
    "mlp" to TheorySpec("Stack simple neurons", "A multilayer perceptron combines weighted sums with nonlinear activations. Backpropagation sends the output error backward through the network to update each weight.", "a₁ = σ(W₁x + b₁)\nŷ = σ(W₂a₁ + b₂)\nW ← W - η ∂L/∂W", listOf("The hidden layer learns intermediate features.", "Backpropagation applies the chain rule efficiently.", "A small hidden layer can represent XOR, which a line cannot separate."), "Learning nonlinear patterns in images, text, signals, and tabular data."),
    "confusion_matrix" to TheorySpec("Count the kinds of predictions", "A confusion matrix compares predicted labels with known labels. Its four counts reveal errors that overall accuracy alone can hide, especially when classes are imbalanced.", "Accuracy = (TP + TN) / N\nPrecision = TP / (TP + FP)\nRecall = TP / (TP + FN)\nF1 = 2PR / (P + R)", listOf("Threshold changes the balance between false positives and false negatives.", "Precision asks how trustworthy positive predictions are.", "Recall asks how many actual positives were found."), "Evaluating binary classifiers and selecting operating thresholds."),
)

@Composable
fun TopicTheoryPage(topicId: String) {
    val spec = theorySpecs.getValue(topicId)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(spec.heading, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(spec.intro, style = MaterialTheme.typography.bodyLarge)
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("The idea in math", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                FormulaView(spec.formula)
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Variations and intuition", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                spec.variants.forEach { variant ->
                    Text("•  $variant", modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Where it is used", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(spec.uses, style = MaterialTheme.typography.bodyLarge)
                Text("Try the Playground →", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}
