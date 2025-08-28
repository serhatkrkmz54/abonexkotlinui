import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abone.abonex.components.bottomnav.BottomNavItem
import com.abone.abonex.components.bottomnav.CustomAppBar
import com.abone.abonex.components.main.HeaderSection
import com.abone.abonex.navigation.AppRoute
import com.abone.abonex.ui.features.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedItemRoute by remember { mutableStateOf("home") }
    var screenTitle by remember { mutableStateOf("Ana Sayfa") }
    Scaffold(
        topBar = {
            uiState.user?.let { user ->
                HeaderSection(
                    fullName = user.firstName,
                    profileImageUrl = user.profileImageUrl
                )
            }
        },
        bottomBar = {
            CustomAppBar(
                onFabClick = { screenTitle = "FAB Tıklandı!" },
                onItemClick = { item ->
                    if (item.route == "logout") {
                        viewModel.logout()
                        navController.navigate(AppRoute.LOGIN_SCREEN) {
                            popUpTo(0)
                        }
                    } else {
                        selectedItemRoute = item.route
                        screenTitle = item.label
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
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }
                uiState.error != null -> {
                    Text(text = "Hata: ${uiState.error}")
                }
                else -> {
                    Text(
                        text = "Seçilen Ekran: $screenTitle",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    }
}