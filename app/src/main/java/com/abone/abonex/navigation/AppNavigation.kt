package com.abone.abonex.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abone.abonex.feature.SplashScreen

@Composable
fun AppNavigation() {
    // Navigasyon denetleyicisini oluşturup hatırlıyoruz.
    // Bu, ekran geçişlerini yönetmemizi sağlar.
    val navController = rememberNavController()

    // NavHost, navigasyon grafiğinin konteyneridir.
    NavHost(
        navController = navController,
        startDestination = AppRoute.SPLASH_SCREEN // Uygulama açıldığında ilk bu ekran gösterilecek
    ) {
        // Splash ekranı için rota tanımı
        composable(route = AppRoute.SPLASH_SCREEN) {
            SplashScreen(
                onStartClick = {
                    // Butona tıklandığında Home ekranına git
                    navController.navigate(AppRoute.HOME_SCREEN) {
                        // Splash ekranını geri yığınından (back stack) kaldır.
                        // Böylece kullanıcı geri tuşuna basınca splash ekranına dönmez.
                        popUpTo(AppRoute.SPLASH_SCREEN) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Home ekranı için rota tanımı
        composable(route = AppRoute.HOME_SCREEN) {
            HomeScreen()
        }
    }
}