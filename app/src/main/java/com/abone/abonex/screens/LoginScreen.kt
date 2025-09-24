package com.abone.abonex.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abone.abonex.R
import com.abone.abonex.navigation.AppRoute
import com.abone.abonex.ui.features.LoginResult
import com.abone.abonex.ui.features.LoginViewModel
import com.abone.abonex.ui.theme.InputBorder
import com.abone.abonex.ui.theme.poppins

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    var isPasswordVisible by remember { mutableStateOf(false) }

    val loginState by viewModel.loginState.collectAsState()
    var showReactivateDialog by remember { mutableStateOf(false) }

    val isButtonEnabled = remember(viewModel.email, viewModel.password, loginState) {
        viewModel.email.trim().isNotEmpty() &&
                viewModel.password.isNotEmpty() &&
                loginState !is LoginResult.Loading
    }
    val errorMessage = if (loginState is LoginResult.Error && !(loginState as LoginResult.Error).isAccountInactive) {
        (loginState as LoginResult.Error).message
    } else {
        null
    }
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginResult.Success -> {
                navController.navigate(AppRoute.HOME_SCREEN) {
                    popUpTo(AppRoute.LOGIN_SCREEN) { inclusive = true }
                }
            }
            is LoginResult.Error -> {
                if (state.isAccountInactive) {
                    showReactivateDialog = true
                }
            }
            is LoginResult.OtpSent -> {
                navController.navigate("${AppRoute.OTP_SCREEN}/${state.email}/${state.password}")
            }
            else -> {}
        }
    }
    if (showReactivateDialog) {
        AlertDialog(
            onDismissRequest = { showReactivateDialog = false },
            title = { Text("Hesap Aktifleştirme") },
            text = { Text("Hesabınız pasif durumdadır. Yeniden aktifleştirmek ister misiniz?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.requestReactivationOtp()
                    showReactivateDialog = false
                }) { Text("Evet, Kod Gönder") }
            },
            dismissButton = {
                TextButton(onClick = { showReactivateDialog = false }) { Text("Vazgeç") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.splash_logo_800x),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Giriş Yap",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontFamily = poppins
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            placeholder = { Text("Email", color = Color.Gray) },
            isError = errorMessage != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = InputBorder,
                unfocusedContainerColor = InputBorder,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFF6759FF),
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                errorBorderColor = Color.Red
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            placeholder = { Text("Şifre", color = Color.Gray) },
            isError = errorMessage != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = InputBorder,
                unfocusedContainerColor = InputBorder,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFF6759FF),
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                errorBorderColor = Color.Red
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (isPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = null, tint = Color.White)
                }
            }
        )

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.login() },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6759FF), contentColor = Color.White),
            enabled = isButtonEnabled
        ) {
            if (loginState is LoginResult.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Giriş Yap", fontWeight = FontWeight.Bold)
            }
        }

        Row(
            modifier = Modifier.padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Hesabın yok mu? ", fontSize = 14.sp, fontFamily = poppins, color = Color.White)
            Text(
                text = "Kayıt Ol",
                fontSize = 14.sp,
                fontFamily = poppins,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF9800),
                modifier = Modifier.clickable { navController.navigate(AppRoute.REGISTER_SCREEN) }
            )
        }
    }
}