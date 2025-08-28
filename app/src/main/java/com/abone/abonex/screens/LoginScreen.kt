package com.abone.abonex.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.abone.abonex.ui.features.LoginViewModel
import com.abone.abonex.ui.theme.InputBorder
import com.abone.abonex.ui.theme.poppins
import com.abone.abonex.util.Resource

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val loginState by viewModel.loginState.collectAsState()
    val isButtonEnabled = remember(email, password, loginState) {
        email.trim().isNotEmpty() &&
                password.isNotEmpty() &&
                loginState !is Resource.Loading
    }
    val isError = loginState is Resource.Error

    LaunchedEffect(loginState) {
        if (loginState is Resource.Success) {
            navController.navigate(AppRoute.HOME_SCREEN) {
                popUpTo(AppRoute.LOGIN_SCREEN) { inclusive = true }
            }
        }
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
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email", color = Color.Gray) },
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBorder,
                unfocusedContainerColor = InputBorder,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,

                focusedIndicatorColor = Color(0xFF6759FF),
                unfocusedIndicatorColor = Color.Gray.copy(alpha = 0.5f),
                errorIndicatorColor = Color.Red
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Şifre", color = Color.Gray) },
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBorder,
                unfocusedContainerColor = InputBorder,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,

                focusedIndicatorColor = Color(0xFF6759FF),
                unfocusedIndicatorColor = Color.Gray.copy(alpha = 0.5f),
                errorIndicatorColor = Color.Red
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

        Spacer(modifier = Modifier.height(24.dp))

        if (loginState is Resource.Error) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Hata",
                    tint = Color.Red
                )
                Text(
                    text = (loginState as Resource.Error).message ?: "Bilinmeyen hata",
                    color = Color.Red
                )
            }
        }

        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6759FF),
                contentColor = Color.White
            ),
            enabled = isButtonEnabled
        ) {
            Text("Giriş Yap", fontWeight = FontWeight.Bold)
        }

        if (loginState is Resource.Loading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(color = Color.White)
        }

        Row(
            modifier = Modifier
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.login_screen_kayit1),
                fontSize = 14.sp,
                fontFamily = poppins,
                color = Color.White
            )
            Text(
                text = stringResource(R.string.login_screen_kayitol),
                fontSize = 14.sp,
                fontFamily = poppins,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF9800),
                modifier = Modifier.clickable {
                    navController.navigate(AppRoute.REGISTER_SCREEN)
                }
            )
        }
    }
}
