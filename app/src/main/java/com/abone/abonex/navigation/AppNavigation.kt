package com.abone.abonex.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abone.abonex.feature.SplashScreen
import com.abone.abonex.screens.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoute.SPLASH_SCREEN
    ) {
        composable(route = AppRoute.SPLASH_SCREEN) {
            // SplashScreen'e navController'ı doğrudan veriyoruz.
            SplashScreen(navController = navController)
        }

        composable(route = AppRoute.LOGIN_SCREEN) {
            LoginScreen(navController = navController)
        }

        composable(route = AppRoute.HOME_SCREEN) {
            HomeScreen(navController = navController)
        }
    }
}