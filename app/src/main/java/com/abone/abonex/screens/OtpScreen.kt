package com.abone.abonex.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abone.abonex.navigation.AppRoute
import com.abone.abonex.ui.features.OtpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpScreen(
    navController: NavController,
    viewModel: OtpViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var otpCode by remember { mutableStateOf("") }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.navigate(AppRoute.HOME_SCREEN) {

                popUpTo(0) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Hesabı Aktifleştir") }) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Lütfen ${viewModel.email} adresine gönderilen 4 haneli kodu girin.",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = otpCode,
                onValueChange = {
                    if (it.length <= 4) {
                        otpCode = it.filter { char -> char.isDigit() }
                    }
                },
                label = { Text("Doğrulama Kodu") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                singleLine = true
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { viewModel.verifyOtp(otpCode) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !state.isLoading && otpCode.length == 4
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Doğrula ve Giriş Yap")
                }
            }
            state.error?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            TextButton(
                onClick = { viewModel.resendOtp() },
                enabled = state.canResend && !state.isLoading
            ) {
                Text(
                    if (state.canResend) "Kodu Tekrar Gönder"
                    else "Tekrar göndermek için: ${state.resendTimer}s"
                )
            }
        }
    }
}