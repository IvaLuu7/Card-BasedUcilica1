package com.example.card_baseducilica.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.card_baseducilica.ui.*
import com.example.card_baseducilica.viewmodel.LearningViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val SETS = "sets"
    const val CARDS_ROUTE = "cards"
    const val LEARNING = "learning"
    const val RESULT = "result"
    const val IMPORT_SET = "import_set"

}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                },
                onLoginSuccess = {
                    navController.navigate(Routes.SETS) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.SETS) {
            MySetsScreen(navController = navController)
        }

        composable(
            route = "${Routes.CARDS_ROUTE}/{setId}/{setTitle}"
        ) { backStackEntry ->

            val setId = backStackEntry.arguments
                ?.getString("setId")
                ?.toIntOrNull() ?: return@composable

            val setTitle = backStackEntry.arguments
                ?.getString("setTitle") ?: ""

            CardsScreen(
                navController = navController,
                setId = setId,
                setTitle = setTitle
            )
        }

        composable(
            route = "${Routes.LEARNING}/{setId}/{setTitle}/{onlyFavorites}"
        ) { backStackEntry ->

            val setId = backStackEntry.arguments
                ?.getString("setId")
                ?.toIntOrNull() ?: return@composable

            val setTitle = backStackEntry.arguments
                ?.getString("setTitle") ?: ""

            val onlyFavorites = backStackEntry.arguments
                ?.getString("onlyFavorites")
                ?.toBooleanStrictOrNull() ?: false

            LearningScreen(
                setId = setId,
                setTitle = setTitle,
                onlyFavorites = onlyFavorites,
                onFinished = {
                    navController.navigate(Routes.RESULT)
                },
                onCancel = {
                    navController.popBackStack(Routes.SETS, false)
                }
            )
        }

        composable(Routes.RESULT) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(
                    "${Routes.LEARNING}/{setId}/{setTitle}/{onlyFavorites}"
                )
            }

            val learningViewModel: LearningViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(parentEntry)

            LearningResultScreen(
                viewModel = learningViewModel,
                onBackToSets = {
                    navController.navigate(Routes.SETS) {
                        popUpTo(Routes.SETS) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.IMPORT_SET) {
            ImportSetScreen(navController)
        }

    }
}