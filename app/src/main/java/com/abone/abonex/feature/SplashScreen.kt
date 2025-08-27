package com.abone.abonex.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abone.abonex.R
import com.abone.abonex.ui.theme.poppins

@Composable
@Preview
fun SplashScreen(onStartClick: () -> Unit ={}) {
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
                .padding(start = 24.dp, end = 24.dp, bottom = 180.dp),
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
        Button(onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(60.dp)
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
    }
}