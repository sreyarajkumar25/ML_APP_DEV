package com.yourname.mllearn.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yourname.mllearn.data.AlgorithmConfig
import com.yourname.mllearn.data.ALL_TOPICS
import com.yourname.mllearn.topics.common.ParameterSetupScreen
import com.yourname.mllearn.topics.common.TopicLessonScreen
import com.yourname.mllearn.ui.home.HomeScreen
import com.yourname.mllearn.ui.home.LandingScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "landing") {
        composable("landing") {
            LandingScreen { navController.navigate("home") }
        }
        composable("home") {
            HomeScreen { topic -> navController.navigate("setup/${topic.id}") }
        }
        composable(
            route = "setup/{topicId}",
            arguments = listOf(navArgument("topicId") { type = NavType.StringType })
        ) { entry ->
            val topic = ALL_TOPICS.firstOrNull { it.id == entry.arguments?.getString("topicId") }
            if (topic != null && topic.available) {
                ParameterSetupScreen(topic, onBack = { navController.popBackStack() }) { config ->
                    navController.navigate("lesson/${topic.id}/${config.toRouteValue()}")
                }
            } else {
                HomeScreen { selected -> navController.navigate("setup/${selected.id}") }
            }
        }
        composable(
            route = "lesson/{topicId}/{config}",
            arguments = listOf(
                navArgument("topicId") { type = NavType.StringType },
                navArgument("config") { type = NavType.StringType }
            )
        ) { entry ->
            val topic = ALL_TOPICS.firstOrNull { it.id == entry.arguments?.getString("topicId") }
            if (topic != null && topic.available) {
                TopicLessonScreen(topic, AlgorithmConfig.fromRouteValue(entry.arguments?.getString("config"))) {
                    navController.popBackStack()
                }
            } else {
                HomeScreen { selected -> navController.navigate("setup/${selected.id}") }
            }
        }
    }
}
