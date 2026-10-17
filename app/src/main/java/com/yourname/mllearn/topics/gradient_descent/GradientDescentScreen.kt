package com.yourname.mllearn.topics.gradient_descent

import androidx.compose.runtime.Composable
import com.yourname.mllearn.data.AlgorithmConfig
import com.yourname.mllearn.ui.components.TopicScaffold

@Composable
fun GradientDescentScreen(onBack: () -> Unit, config: AlgorithmConfig = AlgorithmConfig()) {
    TopicScaffold(
        title = "Gradient Descent",
        emoji = "⛰️",
        onBack = onBack,
        theoryContent = { GradientDescentTheory() },
        playgroundContent = { GradientDescentPlayground(config) }
    )
}
