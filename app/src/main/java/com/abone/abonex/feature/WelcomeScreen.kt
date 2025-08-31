package com.abone.abonex.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abone.abonex.R
import com.abone.abonex.navigation.AppRoute
import com.abone.abonex.ui.features.SplashViewModel
import com.abone.abonex.ui.theme.poppins

@Composable
fun WelcomeScreen(navController: NavController) {

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.splash_bg))
    ) {
        Image(painterResource(R.drawable.splash_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
            )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 220.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.splash_first_text),
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = poppins,
                color = colorResource(R.color.white)
            )
            Text(
                text = stringResource(R.string.splash_second_text),
                fontSize = 25.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = poppins,
                color = colorResource(R.color.white),

            )
            Text(
                text = stringResource(R.string.splash_third_text),
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                fontFamily = poppins,
                color = colorResource(R.color.white),
                modifier = Modifier
                    .padding(top = 10.dp)
            )
        }
        Button(onClick = {
            navController.navigate(AppRoute.REGISTER_SCREEN) {
                popUpTo(AppRoute.WELCOME_SCREEN) { inclusive = true }}
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 120.dp, horizontal = 24.dp)
                .align(Alignment.BottomCenter)
                .height(50.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = stringResource(R.string.splash_button_text),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = poppins,
                color = colorResource(R.color.splash_button_text)
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 70.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.welcome_screen_uyelik1),
                fontSize = 14.sp,
                fontFamily = poppins,
                color = Color.White
            )
            Text(
                text = stringResource(R.string.welcome_screen_giris),
                fontSize = 14.sp,
                fontFamily = poppins,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF9800),
                modifier = Modifier.clickable {
                    navController.navigate(AppRoute.LOGIN_SCREEN)
                }
            )
        }
    }
}