package com.abone.abonex.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abone.abonex.feature.SplashScreen
import com.abone.abonex.feature.WelcomeScreen
import com.abone.abonex.screens.AddSubscriptionScreen
import com.abone.abonex.screens.LoginScreen
import com.abone.abonex.screens.ProfileScreen
import com.abone.abonex.screens.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoute.SPLASH_SCREEN
    ) {
        composable(route = AppRoute.SPLASH_SCREEN) {
            SplashScreen(navController = navController)
        }

        composable(route = AppRoute.WELCOME_SCREEN) {
            WelcomeScreen(navController = navController)
        }

        composable(route = AppRoute.REGISTER_SCREEN) {
            RegisterScreen(navController = navController)
        }

        composable(route = AppRoute.LOGIN_SCREEN) {
            LoginScreen(navController = navController)
        }

        composable(route = AppRoute.HOME_SCREEN) {
            HomeScreen(navController = navController)
        }
        composable(AppRoute.PROFILE_SCREEN) {
            ProfileScreen(navController = navController)
        }
        composable(route = AppRoute.ADD_SUBSCRIPTION_SCREEN) {
            AddSubscriptionScreen(navController = navController)
        }
    }
}