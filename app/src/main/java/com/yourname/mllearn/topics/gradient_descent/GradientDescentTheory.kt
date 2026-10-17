package com.yourname.mllearn.topics.gradient_descent

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

@Composable
fun GradientDescentTheory() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("A downhill walk", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(
                    "Gradient descent is a way to improve a model by repeatedly adjusting its parameters in the direction that reduces its error. Imagine standing on a bowl-shaped hill: the gradient tells you which way is uphill, so you step the other way.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionTitle("Our toy loss")
                Text("There is one weight, w, and a target value. The best weight is the target because that makes the squared error zero.", style = MaterialTheme.typography.bodyLarge)
                FormulaView("L(w) = (w - target)²\n∂L/∂w = 2(w - target)\nw ← w - η · ∂L/∂w")
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionTitle("What changes the walk?")
                Text("The learning rate η controls step size. A value that is too small moves slowly; a value that is too large can jump past the minimum.", style = MaterialTheme.typography.bodyLarge)
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionTitle("Common variants")
                Bullet("Batch: averages the gradient over the full training set before each update.")
                Bullet("Stochastic (SGD): uses one training example per update, making steps noisy but frequent.")
                Bullet("Mini-batch: averages a small group of examples for a practical balance.")
                Text("In this one-example demo, all three variants see the same gradient and therefore follow the same path.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionTitle("Where it is used")
                Text("Gradient descent trains linear and logistic regression, neural networks, and many other models by minimizing a chosen loss function.", style = MaterialTheme.typography.bodyLarge)
                Text("Try the Playground →", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
}

@Composable
private fun Bullet(text: String) {
    Text("•  $text", modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyLarge)
}
