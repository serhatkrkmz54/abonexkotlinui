package com.abone.abonex.components.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderSection(
    fullName: String,
    profileImageUrl: String?,
    unreadCount: Int,
    isLoading: Boolean, // YENİ: Yüklenme durumunu almak için
    onNotificationClick: () -> Unit,
    onRefreshClick: () -> Unit, // YENİ: Yenileme butonuna tıklandığında
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Merhaba,", style = MaterialTheme.typography.titleMedium)
            Text(text = fullName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }

        // Bildirimler ikonu ve rozeti
        BadgedBox(
            badge = {
                if (unreadCount > 0) {
                    Badge { Text(text = unreadCount.toString()) }
                }
            }
        ) {
            IconButton(onClick = onNotificationClick) {
                Icon(Icons.Default.Notifications, contentDescription = "Bildirimler")
            }
        }

        // YENİ: Akıllı Yenileme Butonu
        // Crossfade, iki bileşen arasında yumuşak bir geçiş animasyonu sağlar.
        Crossfade(targetState = isLoading, label = "refresh-indicator") { loading ->
            if (loading) {
                // Yükleniyorsa, animasyon göster
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp).padding(start = 8.dp),
                    strokeWidth = 2.dp
                )
            } else {
                // Yüklenmiyorsa, yenileme ikonunu göster
                IconButton(onClick = onRefreshClick) {
                    Icon(Icons.Default.Refresh, contentDescription = "Yenile")
                }
            }
        }
    }
}
