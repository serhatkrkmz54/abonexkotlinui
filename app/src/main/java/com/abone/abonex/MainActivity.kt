package com.abone.abonex

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.abone.abonex.domain.enums.NotificationType
import com.abone.abonex.navigation.AppNavigation
import com.abone.abonex.navigation.AppRoute
import com.abone.abonex.ui.theme.AboneXTheme
import com.abone.abonex.util.SnackbarManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var snackbarManager: SnackbarManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AboneXTheme(darkTheme = true) {
                val snackbarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    snackbarManager.messages.collect { messagePair ->
                        messagePair?.let {
                            snackbarHostState.showSnackbar(it.first)
                            snackbarManager.clearMessage()
                        }
                    }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigation(navController = navController)
                        CheckForNotificationIntent(navController = navController, intent = intent)

                        NotificationPermissionHandler(snackbarHostState = snackbarHostState)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationPermissionHandler(snackbarHostState: SnackbarHostState) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            scope.launch {
                snackbarHostState.showSnackbar("Bildirimler aktifleştirildi!")
            }
        } else {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "Bildirimlere izin vermediniz. Hatırlatıcıları kaçırabilirsiniz.",
                    actionLabel = "Ayarlar",
                    duration = SnackbarDuration.Long
                )
                if (result == SnackbarResult.ActionPerformed) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Composable
private fun CheckForNotificationIntent(navController: NavController, intent: Intent) {
    LaunchedEffect(key1 = intent) {
        val notificationTypeStr = intent.getStringExtra("notificationType")
        if (notificationTypeStr != null) {
            val notificationType = try {
                NotificationType.valueOf(notificationTypeStr)
            } catch (e: Exception) { NotificationType.UNKNOWN }

            val subscriptionId = intent.getStringExtra("subscriptionId")

            when (notificationType) {
                NotificationType.UPCOMING_PAYMENT,
                NotificationType.PAYMENT_OVERDUE,
                NotificationType.SUBSCRIPTION_EXPIRED,
                NotificationType.PAYMENT_CONFIRMED -> {
                    if (subscriptionId != null) {
                        navController.navigate("${AppRoute.SUBSCRIPTION_DETAIL_SCREEN}/$subscriptionId")
                    }
                }

                NotificationType.SUBSCRIPTION_CANCELLED -> {
                    navController.navigate(AppRoute.HOME_SCREEN) {
                        popUpTo(0)
                    }
                }

                else -> { /* Diğer tipler için özel bir aksiyon yok */ }
            }

            intent.removeExtra("notificationType")
            intent.removeExtra("subscriptionId")
        }
    }
}