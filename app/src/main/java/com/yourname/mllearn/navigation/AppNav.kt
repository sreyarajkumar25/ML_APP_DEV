package com.yourname.mllearn.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yourname.mllearn.data.ALL_TOPICS
import com.yourname.mllearn.topics.gradient_descent.GradientDescentScreen
import com.yourname.mllearn.ui.home.HomeScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen { topic -> navController.navigate("topic/${topic.id}") }
        }
        composable(
            route = "topic/{topicId}",
            arguments = listOf(navArgument("topicId") { type = NavType.StringType })
        ) { entry ->
            val topic = ALL_TOPICS.firstOrNull { it.id == entry.arguments?.getString("topicId") }
            if (topic?.available == true && topic.id == "gradient_descent") {
                GradientDescentScreen(onBack = { navController.popBackStack() })
            } else {
                navController.popBackStack()
            }
        }
    }
}
