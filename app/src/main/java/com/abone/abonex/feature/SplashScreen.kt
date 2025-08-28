package com.abone.abonex.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abone.abonex.R
import com.abone.abonex.navigation.AppRoute
import com.abone.abonex.ui.features.SplashViewModel
import com.abone.abonex.ui.theme.AppBackground

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val startDestination by viewModel.startDestination.collectAsState()

    LaunchedEffect(startDestination) {
        startDestination?.let { route ->
            navController.navigate(route) {
                popUpTo(AppRoute.SPLASH_SCREEN) {
                    inclusive = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_logo_800x),
            contentDescription = "Uygulama Logosu"
        )
    }
}