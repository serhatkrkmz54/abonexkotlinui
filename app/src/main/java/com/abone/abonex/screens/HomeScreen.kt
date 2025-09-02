import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abone.abonex.R
import com.abone.abonex.components.bottomnav.CustomAppBar
import com.abone.abonex.components.main.EmptyContent
import com.abone.abonex.components.main.HeaderSection
import com.abone.abonex.components.main.MonthlySpendCard
import com.abone.abonex.components.main.SubscriptionList
import com.abone.abonex.navigation.AppRoute
import com.abone.abonex.ui.features.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var screenTitle by remember { mutableStateOf("Ana Sayfa") }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            // DEĞİŞİKLİK 1: Üst köşeleri daha oval yapıyoruz
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            // DEĞİŞİKLİK 2: Temayla uyumlu bir arkaplan rengi belirliyoruz
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            // DEĞİŞİKLİK 3: Menü içeriğine daha iyi bir yapı ve boşluk veriyoruz
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                // Drag Handle (tutma çubuğu) için üstte boşluk bırak
                Spacer(modifier = Modifier.height(16.dp))

                BottomSheetOption(
                    icon = Icons.Default.AddCard,
                    text = "Yeni Abonelik Oluştur",
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                                navController.navigate(AppRoute.ADD_SUBSCRIPTION_SCREEN)
                            }
                        }
                    }
                )

                BottomSheetOption(
                    icon = Icons.AutoMirrored.Filled.ListAlt,
                    text = "Hazır Aboneliklerden Oluştur",
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                                println("Hazır Abonelikler ekranına gidilecek.")
                            }
                        }
                    }
                )
            }
        }
    }

    var selectedItemRoute by remember { mutableStateOf("home") }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                uiState.user?.let { user ->
                    HeaderSection(
                        fullName = user.firstName,
                        profileImageUrl = user.profileImageUrl
                    )
                }
                if (uiState.subscriptions.isNotEmpty()) {
                    uiState.monthlySpend?.let { spend ->
                        MonthlySpendCard(
                            amount = spend.totalAmount,
                            currency = spend.currency,
                            month = spend.month.name,
                            cardTypeLogo = R.drawable.mastercard_icon
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        bottomBar = {
            CustomAppBar(
                onFabClick = { showBottomSheet = true },
                onItemClick = { item ->
                    when (item.route) {
                        "logout" -> {
                            viewModel.logout()
                            navController.navigate(AppRoute.LOGIN_SCREEN) { popUpTo(0) }
                        }
                        "profile" -> navController.navigate(AppRoute.PROFILE_SCREEN)
                        else -> {
                            selectedItemRoute = item.route
                        }
                    }
                },
                currentRoute = selectedItemRoute
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator()
                uiState.error != null -> Text(text = "Hata: ${uiState.error}")
                uiState.subscriptions.isNotEmpty() -> SubscriptionList(subscriptions = uiState.subscriptions)
                else -> {
                    if (selectedItemRoute == "home") {
                        EmptyContent(
                            onAddClick = { showBottomSheet = true }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomSheetOption(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp), // Dikey boşluğu artırdık
        verticalAlignment = Alignment.CenterVertically
    ) {
        // İkonu renkli bir daire içine alıyoruz
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(22.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}