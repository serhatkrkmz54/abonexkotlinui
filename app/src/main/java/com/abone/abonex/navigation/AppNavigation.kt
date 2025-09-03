package com.abone.abonex.navigation

import HomeScreen
import android.R.attr.type
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.abone.abonex.feature.SplashScreen
import com.abone.abonex.feature.WelcomeScreen
import com.abone.abonex.screens.AddSubscriptionScreen
import com.abone.abonex.screens.AllSubscriptionsScreen
import com.abone.abonex.screens.LoginScreen
import com.abone.abonex.screens.ProfileScreen
import com.abone.abonex.screens.RegisterScreen
import com.abone.abonex.screens.SubscriptionDetailScreen
import com.abone.abonex.screens.TemplateConfirmationScreen

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
        composable(route = AppRoute.ALL_SUBSCRIPTIONS_SCREEN) {
            AllSubscriptionsScreen(navController = navController)
        }
        composable(
            route = "${AppRoute.TEMPLATE_CONFIRM_SCREEN}/{planId}",
            arguments = listOf(
                navArgument("planId") { type = NavType.LongType }
            )
        ) {
            TemplateConfirmationScreen(navController = navController)
        }
        composable(
            route = "${AppRoute.SUBSCRIPTION_DETAIL_SCREEN}/{subscriptionId}",
            arguments = listOf(navArgument("subscriptionId") { type = NavType.LongType })
        ) {
            SubscriptionDetailScreen(navController = navController)
        }
    }
}