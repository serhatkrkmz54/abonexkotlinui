package com.abone.abonex.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.abone.abonex.R
import com.abone.abonex.data.remote.dto.UserDto
import com.abone.abonex.navigation.AppRoute
import com.abone.abonex.ui.features.ProfileViewModel
import com.abone.abonex.ui.theme.AppBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = AppBackground
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
                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                uiState.user != null -> {
                    // ProfileContent'e artık NavController'ı da gönderiyoruz.
                    ProfileContent(
                        user = uiState.user!!,
                        navController = navController,
                        onLogout = {
                            viewModel.logout()
                            navController.navigate(AppRoute.LOGIN_SCREEN) {
                                popUpTo(0)
                            }
                        }
                    )
                }
            }
        }
    }
}

/**
 * Veri başarıyla yüklendiğinde gösterilecek olan profil içeriği.
 * Artık geri butonunu da içeriyor.
 */
@Composable
private fun ProfileContent(
    user: UserDto,
    onLogout: () -> Unit,
    navController: NavController, // Geri butonu için NavController eklendi
    modifier: Modifier = Modifier
) {
    // Geri butonunu içeriğin üzerine katman olarak koyabilmek için Box kullanıyoruz.
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // --- BÖLÜM 1: ARKA PLAN RESMİ OLAN ÜST KISIM ---
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Arka plan resmi
                Image(
                    painter = painterResource(id = R.drawable.user_bg),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )

                // Resmin üzerindeki içerik
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = user.profileImageUrl ?: R.drawable.profile_default,
                        contentDescription = "Profil Resmi",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // --- BÖLÜM 2: ESKİ ARKA PLANA SAHİP ALT KISIM ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                ProfileInfoField(label = "Email", value = user.email)
                Spacer(modifier = Modifier.height(16.dp))
                ProfileInfoField(label = "Phone", value = user.phoneNumber ?: "-")

                Spacer(modifier = Modifier.weight(1f))

                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                ) {
                    Text("Logout", fontSize = 16.sp)
                }
            }
        }

        // --- GERİ BUTONU ---
        // Box'ın bir öğesi olarak en üste ekleniyor.
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier
                .align(Alignment.TopStart) // Sol üste hizala
                .statusBarsPadding() // Telefonun durum çubuğunun altına it
                .padding(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Geri",
                tint = Color.White
            )
        }
    }
}

/**
 * "Email" ve "Phone" gibi read-only alanlar için yeniden kullanılabilir component.
 */
@Composable
private fun ProfileInfoField(label: String, value: String) {
    OutlinedTextField(
        value = value,
        onValueChange = { },
        label = { Text(label) },
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.White,
            disabledLabelColor = Color.Gray,
            disabledContainerColor = Color.White.copy(alpha = 0.05f),
            disabledIndicatorColor = Color.Transparent,
            disabledLeadingIconColor = Color.Gray,
        ),
        enabled = false
    )
}