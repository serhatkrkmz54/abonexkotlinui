package com.abone.abonex.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
        label = "Profil",
        icon = Icons.Default.Person,
        route = "profile"
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
                .height(75.dp)
                .clip(CurvedBottomBarShape()),
            containerColor = colorResource(R.color.bottom_bar_bg),
            tonalElevation = 10.dp
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

                    NavigationBarItem(
                        selected = currentRoute == item.route,
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
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorResource(R.color.bottom_bar_buttons_hover),
                            selectedTextColor = colorResource(R.color.bottom_bar_buttons_hover),
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f),
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = onFabClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-18).dp)
                .size(64.dp),
            shape = CircleShape,
            containerColor = colorResource(R.color.bottom_bar_plus_icon),
            contentColor = MaterialTheme.colorScheme.onSecondary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Abonelik Ekle")
        }
    }
}