package com.example.card_baseducilica.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.card_baseducilica.ui.LoginScreen
import com.example.card_baseducilica.ui.RegisterScreen
import com.example.card_baseducilica.ui.MySetsScreen
import com.example.card_baseducilica.ui.CardsScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val SETS = "sets"

    const val CARDS_ROUTE = "cards"
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
            route = "${Routes.CARDS_ROUTE}/{setId}/{setTitle}",
        ) { backStackEntry ->

            val setId = backStackEntry.arguments
                ?.getString("setId")
                ?.toIntOrNull() ?: return@composable

            val setTitle = backStackEntry.arguments
                ?.getString("setTitle") ?: ""

            CardsScreen(
                setId = setId,
                setTitle = setTitle
            )
        }
    }
}