package com.yourname.mllearn.topics.common

import androidx.compose.runtime.Composable
import com.yourname.mllearn.data.AlgorithmConfig
import com.yourname.mllearn.data.Topic
import com.yourname.mllearn.topics.bias_variance.BiasVariancePlayground
import com.yourname.mllearn.topics.bias_variance.BiasVarianceTheory
import com.yourname.mllearn.topics.confusion_matrix.ConfusionMatrixPlayground
import com.yourname.mllearn.topics.confusion_matrix.ConfusionMatrixTheory
import com.yourname.mllearn.topics.gradient_descent.GradientDescentScreen
import com.yourname.mllearn.topics.kmeans.KMeansPlayground
import com.yourname.mllearn.topics.kmeans.KMeansTheory
import com.yourname.mllearn.topics.linear_regression.LinearRegressionPlayground
import com.yourname.mllearn.topics.linear_regression.LinearRegressionTheory
import com.yourname.mllearn.topics.logistic_regression.LogisticRegressionPlayground
import com.yourname.mllearn.topics.logistic_regression.LogisticRegressionTheory
import com.yourname.mllearn.topics.loss_functions.LossFunctionsPlayground
import com.yourname.mllearn.topics.loss_functions.LossFunctionsTheory
import com.yourname.mllearn.topics.mlp.MlpPlayground
import com.yourname.mllearn.topics.mlp.MlpTheory
import com.yourname.mllearn.topics.perceptron.PerceptronPlayground
import com.yourname.mllearn.topics.perceptron.PerceptronTheory
import com.yourname.mllearn.topics.spectral_clustering.SpectralClusteringPlayground
import com.yourname.mllearn.topics.spectral_clustering.SpectralClusteringTheory
import com.yourname.mllearn.ui.components.TopicScaffold

@Composable
fun TopicLessonScreen(topic: Topic, config: AlgorithmConfig = AlgorithmConfig(), onBack: () -> Unit) {
    when (topic.id) {
        "gradient_descent" -> GradientDescentScreen(onBack, config)
        "loss_functions" -> TopicScaffold(topic.title, topic.emoji, onBack, { LossFunctionsTheory() }, { LossFunctionsPlayground(config) })
        "bias_variance" -> TopicScaffold(topic.title, topic.emoji, onBack, { BiasVarianceTheory() }, { BiasVariancePlayground(config) })
        "linear_regression" -> TopicScaffold(topic.title, topic.emoji, onBack, { LinearRegressionTheory() }, { LinearRegressionPlayground(config) })
        "logistic_regression" -> TopicScaffold(topic.title, topic.emoji, onBack, { LogisticRegressionTheory() }, { LogisticRegressionPlayground(config) })
        "kmeans" -> TopicScaffold(topic.title, topic.emoji, onBack, { KMeansTheory() }, { KMeansPlayground(config) })
        "spectral_clustering" -> TopicScaffold(topic.title, topic.emoji, onBack, { SpectralClusteringTheory() }, { SpectralClusteringPlayground(config) })
        "perceptron" -> TopicScaffold(topic.title, topic.emoji, onBack, { PerceptronTheory() }, { PerceptronPlayground(config) })
        "mlp" -> TopicScaffold(topic.title, topic.emoji, onBack, { MlpTheory() }, { MlpPlayground(config) })
        "confusion_matrix" -> TopicScaffold(topic.title, topic.emoji, onBack, { ConfusionMatrixTheory() }, { ConfusionMatrixPlayground(config) })
        else -> GradientDescentScreen(onBack)
    }
}
