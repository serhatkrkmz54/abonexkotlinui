package com.abone.abonex.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abone.abonex.navigation.AppRoute
import com.abone.abonex.ui.features.LoginViewModel
import com.abone.abonex.util.Resource

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    // UI üzerindeki anlık verileri tutacak state'ler
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // ViewModel'den gelen login durumunu dinliyoruz
    val loginState by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = loginState) {
        if (loginState is Resource.Success) {
            // Toast.makeText(context, "Giriş Başarılı!", Toast.LENGTH_SHORT).show()
            // Başarılı giriş sonrası Home ekranına yönlendir
            navController.navigate(AppRoute.HOME_SCREEN) {
                // Login ekranını geri yığınından kaldır
                popUpTo(AppRoute.LOGIN_SCREEN) {
                    inclusive = true
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Giriş Yap", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(32.dp))

            // Email Giriş Alanı
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Şifre Giriş Alanı
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Şifre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (isPasswordVisible) Icons.Filled.Email else Icons.Filled.Face
                    val description = if (isPasswordVisible) "Şifreyi gizle" else "Şifreyi göster"
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Giriş Butonu
            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth(),
                // API isteği devam ederken butonu pasif yap
                enabled = loginState !is Resource.Loading
            ) {
                Text(text = "Giriş Yap")
            }

            // Hata Mesajı Alanı
            if (loginState is Resource.Error) {
                val errorMessage = (loginState as Resource.Error).message ?: "Bilinmeyen hata"
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Yükleniyor durumu için Progress Bar
        if (loginState is Resource.Loading) {
            CircularProgressIndicator()
        }
    }
}