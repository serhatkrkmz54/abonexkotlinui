import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.abone.abonex.components.BottomNavItem
import com.abone.abonex.components.CustomAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var selectedItemRoute by remember { mutableStateOf("home") }
    var screenTitle by remember { mutableStateOf("Ana Sayfa") }

    Scaffold(
        bottomBar = {
            CustomAppBar(
                onFabClick = { screenTitle = "FAB Tıklandı!" },
                onItemClick = { item: BottomNavItem ->
                    selectedItemRoute = item.route
                    screenTitle = item.label
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
            Text(
                text = "Seçilen Ekran: $screenTitle",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen()
        }
    }
}