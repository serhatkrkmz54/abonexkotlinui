package com.abone.abonex.components.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.abone.abonex.R
import com.abone.abonex.ui.theme.poppins
import com.abone.abonex.ui.theme.redRose

@Composable
fun HeaderSection(
    fullName: String,
    profileImageUrl: String?
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 17.dp, vertical = 4.dp)
            .fillMaxWidth()
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = profileImageUrl ?: R.drawable.profile_default,
            contentDescription = "Profil Resmi",
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        ) {
            Text(text = "Hoşgeldin $fullName", color = colorResource(R.color.header_text),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = redRose,
                modifier = Modifier
                    .padding(start = 5.dp)
                )
        }
        Icon(Icons.Default.Notifications, contentDescription = "Bildirimler", tint = Color.White, modifier = Modifier.size(25.dp) )
    }
}
