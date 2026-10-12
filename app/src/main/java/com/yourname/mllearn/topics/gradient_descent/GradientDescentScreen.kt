package com.yourname.mllearn.topics.gradient_descent

import androidx.compose.runtime.Composable
import com.yourname.mllearn.ui.components.TopicScaffold

@Composable
fun GradientDescentScreen(onBack: () -> Unit) {
    TopicScaffold(
        title = "Gradient Descent",
        emoji = "⛰️",
        onBack = onBack,
        theoryContent = { GradientDescentTheory() },
        playgroundContent = { GradientDescentPlayground() }
    )
}
