package com.abone.abonex.components.bottomnav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import com.abone.abonex.R
import com.abone.abonex.ui.theme.AppBackground
import com.abone.abonex.ui.theme.poppins

val bottomNavItems = listOf(
    BottomNavItem(
        label = "Ana Sayfa",
        icon = Icons.Default.Home,
        route = "home"
    ),
    BottomNavItem(
        label = "Ayarlar",
        icon = Icons.Default.Settings,
        route = "settings"
    ),
    BottomNavItem(
        label = "Profil",
        icon = Icons.Default.Person,
        route = "profile"
    ),
    BottomNavItem(
        label = "Çıkış Yap",
        icon = Icons.AutoMirrored.Filled.ExitToApp,
        route = "logout"
    )
)


@Composable
fun CustomAppBar(
    onFabClick: () -> Unit,
    onItemClick: (BottomNavItem) -> Unit,
    currentRoute: String?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(103.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(CurvedBottomBarShape())
                .navigationBarsPadding(),
            containerColor = AppBackground,
            tonalElevation = 12.dp,
            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    if (index == 2) {
                        Spacer(modifier = Modifier.width(64.dp))
                    }

                    val colors = if (item.route == "logout") {
                        NavigationBarItemDefaults.colors(
                            selectedIconColor = colorResource(R.color.logout_bg),
                            unselectedIconColor = colorResource(R.color.logout_bg),
                            selectedTextColor = colorResource(R.color.logout_bg),
                            unselectedTextColor = colorResource(R.color.logout_bg),
                            indicatorColor = Color.Transparent
                        )
                    } else {
                        NavigationBarItemDefaults.colors(
                            selectedIconColor = colorResource(R.color.bottom_bar_buttons_hover),
                            selectedTextColor = colorResource(R.color.bottom_bar_buttons_hover),
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f),
                            indicatorColor = Color.Transparent
                        )
                    }

                    NavigationBarItem(
                        selected = if (item.route == "logout") false else currentRoute == item.route,
                        onClick = { onItemClick(item) },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = poppins
                            )
                        },
                        colors = colors
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = onFabClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-34).dp)
                .size(64.dp),
            shape = CircleShape,
            containerColor = colorResource(R.color.bottom_bar_plus_icon),
            contentColor = colorResource(R.color.white)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Abonelik Ekle", modifier = Modifier.size(40.dp))
        }
    }
}