package com.yourname.mllearn.topics.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yourname.mllearn.data.AlgorithmConfig
import com.yourname.mllearn.data.Topic
import java.util.Locale

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ParameterSetupScreen(topic: Topic, onBack: () -> Unit, onStart: (AlgorithmConfig) -> Unit) {
    var config by remember(topic.id) { mutableStateOf(AlgorithmConfig.forTopic(topic.id)) }
    var resetVersion by remember(topic.id) { mutableIntStateOf(0) }
    var invalidFields by remember(topic.id) { mutableStateOf(emptySet<String>()) }
    val update: (AlgorithmConfig) -> Unit = { config = it }
    val onFieldValidityChange: (String, Boolean) -> Unit = { label, invalid ->
        invalidFields = if (invalid) invalidFields + label else invalidFields - label
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("${topic.emoji}  Set up ${topic.title}") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text("Experiment settings", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Choose starting values. You can continue exploring in the playground.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            when (topic.id) {
                "gradient_descent" -> {
                    item { ChoiceRow("Update variant", listOf("BATCH", "SGD", "MINI_BATCH"), config.variant) { update(config.copy(variant = it)) } }
                    item { FloatSetting("Learning rate", config.learningRate, 0.01f..0.95f, resetVersion, { update(config.copy(learningRate = it)) }) { onFieldValidityChange("Learning rate", it) } }
                    item { IntSetting("Iterations", config.iterations, 1..60, resetVersion, { update(config.copy(iterations = it)) }) { onFieldValidityChange("Iterations", it) } }
                    item { FloatSetting("Starting weight", config.startWeight, -5f..5f, resetVersion, { update(config.copy(startWeight = it)) }) { onFieldValidityChange("Starting weight", it) } }
                    item { FloatSetting("Target", config.target, -4f..4f, resetVersion, { update(config.copy(target = it)) }) { onFieldValidityChange("Target", it) } }
                    item { IntSetting("Batch size", config.batchSize, 1..32, resetVersion, { update(config.copy(batchSize = it)) }) { onFieldValidityChange("Batch size", it) } }
                }
                "loss_functions" -> {
                    item { ChoiceRow("Loss", listOf("MSE", "MAE", "Cross-entropy"), config.lossType) { update(config.copy(lossType = it)) } }
                    item { FloatSetting("Prediction error", config.target, 0f..0.95f, resetVersion, { update(config.copy(target = it)) }) { onFieldValidityChange("Prediction error", it) } }
                    item { FloatSetting("Sample weight", config.sampleWeight, 0.25f..2f, resetVersion, { update(config.copy(sampleWeight = it)) }) { onFieldValidityChange("Sample weight", it) } }
                }
                "bias_variance" -> {
                    item { IntSetting("Polynomial degree", config.degree, 1..15, resetVersion, { update(config.copy(degree = it)) }) { onFieldValidityChange("Polynomial degree", it) } }
                    item { FloatSetting("Training noise", config.noise, 0f..0.12f, resetVersion, { update(config.copy(noise = it)) }) { onFieldValidityChange("Training noise", it) } }
                    item { IntSetting("Training points", config.pointCount, 5..60, resetVersion, { update(config.copy(pointCount = it)) }) { onFieldValidityChange("Training points", it) } }
                }
                "linear_regression" -> {
                    item { FloatSetting("Learning rate", config.learningRate, 0.01f..0.8f, resetVersion, { update(config.copy(learningRate = it)) }) { onFieldValidityChange("Learning rate", it) } }
                    item { IntSetting("Iterations", config.iterations, 1..500, resetVersion, { update(config.copy(iterations = it)) }) { onFieldValidityChange("Iterations", it) } }
                    item { FloatSetting("Initial slope", config.startWeight, -2f..2f, resetVersion, { update(config.copy(startWeight = it)) }) { onFieldValidityChange("Initial slope", it) } }
                    item { FloatSetting("Initial intercept", config.startBias, -2f..2f, resetVersion, { update(config.copy(startBias = it)) }) { onFieldValidityChange("Initial intercept", it) } }
                }
                "logistic_regression" -> {
                    item { FloatSetting("Learning rate", config.learningRate, 0.05f..2f, resetVersion, { update(config.copy(learningRate = it)) }) { onFieldValidityChange("Learning rate", it) } }
                    item { IntSetting("Iterations", config.iterations, 10..800, resetVersion, { update(config.copy(iterations = it)) }) { onFieldValidityChange("Iterations", it) } }
                    item { FloatSetting("Decision threshold", config.threshold, 0.1f..0.9f, resetVersion, { update(config.copy(threshold = it)) }) { onFieldValidityChange("Decision threshold", it) } }
                }
                "kmeans" -> {
                    item { IntSetting("Number of clusters K", config.clusters, 2..6, resetVersion, { update(config.copy(clusters = it)) }) { onFieldValidityChange("Number of clusters K", it) } }
                    item { IntSetting("Iterations", config.clusterIterations, 1..12, resetVersion, { update(config.copy(clusterIterations = it)) }) { onFieldValidityChange("Iterations", it) } }
                    item { IntSetting("Initialization seed", config.randomSeed, 0..99, resetVersion, { update(config.copy(randomSeed = it)) }) { onFieldValidityChange("Initialization seed", it) } }
                }
                "spectral_clustering" -> {
                    item { IntSetting("Number of clusters", config.clusters, 2..6, resetVersion, { update(config.copy(clusters = it)) }) { onFieldValidityChange("Number of clusters", it) } }
                    item { IntSetting("Neighbors per point", config.neighbors, 1..5, resetVersion, { update(config.copy(neighbors = it)) }) { onFieldValidityChange("Neighbors per point", it) } }
                }
                "perceptron" -> {
                    item { FloatSetting("Learning rate", config.learningRate, 0.02f..0.8f, resetVersion, { update(config.copy(learningRate = it)) }) { onFieldValidityChange("Learning rate", it) } }
                    item { IntSetting("Epochs", config.iterations, 1..30, resetVersion, { update(config.copy(iterations = it)) }) { onFieldValidityChange("Epochs", it) } }
                }
                "mlp" -> {
                    item { IntSetting("Hidden layers", config.hiddenLayers, 1..3, resetVersion, { update(config.copy(hiddenLayers = it)) }) { onFieldValidityChange("Hidden layers", it) } }
                    item { IntSetting("Neurons per layer", config.neurons, 2..8, resetVersion, { update(config.copy(neurons = it)) }) { onFieldValidityChange("Neurons per layer", it) } }
                    item { FloatSetting("Learning rate", config.learningRate, 0.05f..2f, resetVersion, { update(config.copy(learningRate = it)) }) { onFieldValidityChange("Learning rate", it) } }
                    item { IntSetting("Epochs", config.iterations, 20..1000, resetVersion, { update(config.copy(iterations = it)) }) { onFieldValidityChange("Epochs", it) } }
                }
                "confusion_matrix" -> {
                    item { FloatSetting("Classification threshold", config.threshold, 0.1f..0.9f, resetVersion, { update(config.copy(threshold = it)) }) { onFieldValidityChange("Classification threshold", it) } }
                    item { FloatSetting("Positive class balance", config.classBalance, 0.1f..0.9f, resetVersion, { update(config.copy(classBalance = it)) }) { onFieldValidityChange("Positive class balance", it) } }
                }
            }
            if (invalidFields.isNotEmpty()) {
                item {
                    Text(
                        "Correct the highlighted values before starting.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = {
                        config = AlgorithmConfig.forTopic(topic.id)
                        invalidFields = emptySet()
                        resetVersion++
                    }, modifier = Modifier.weight(1f)) { Text("Reset defaults") }
                    Button(
                        onClick = { onStart(config.validated()) },
                        enabled = invalidFields.isEmpty(),
                        modifier = Modifier.weight(1f)
                    ) { Text("Start") }
                }
            }
        }
    }
}

@Composable
private fun FloatSetting(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    resetVersion: Int,
    onValue: (Float) -> Unit,
    onValidityChange: (Boolean) -> Unit
) {
    var text by remember(label, resetVersion) {
        mutableStateOf(String.format(Locale.US, "%.2f", value))
    }
    OutlinedTextField(
        value = text,
        onValueChange = { input ->
            text = input
            val parsed = input.toFloatOrNull()
            val valid = parsed != null && parsed in range
            onValidityChange(!valid)
            if (parsed != null && valid) onValue(parsed)
        },
        label = { Text(label) },
        supportingText = {
            if (text.toFloatOrNull()?.let { it !in range } != false) {
                Text("Enter a value from ${range.start} to ${range.endInclusive}.")
            }
        },
        isError = text.toFloatOrNull()?.let { it !in range } != false,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (range.start < 0f) KeyboardType.Text else KeyboardType.Decimal
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun IntSetting(
    label: String,
    value: Int,
    range: IntRange,
    resetVersion: Int,
    onValue: (Int) -> Unit,
    onValidityChange: (Boolean) -> Unit
) {
    var text by remember(label, resetVersion) { mutableStateOf(value.toString()) }
    OutlinedTextField(
        value = text,
        onValueChange = { input ->
            text = input
            val parsed = input.toIntOrNull()
            val valid = parsed != null && parsed in range
            onValidityChange(!valid)
            if (parsed != null && valid) onValue(parsed)
        },
        label = { Text(label) },
        supportingText = {
            if (text.toIntOrNull()?.let { it !in range } != false) {
                Text("Enter a whole number from ${range.first} to ${range.last}.")
            }
        },
        isError = text.toIntOrNull()?.let { it !in range } != false,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ChoiceRow(label: String, options: List<String>, selected: String, onSelected: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { option ->
                val display = option.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }
                FilterChip(selected == option, onClick = { onSelected(option) }, label = { Text(display) })
            }
        }
    }
}