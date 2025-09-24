package com.abone.abonex.components.bottomnav

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.abone.abonex.ui.theme.expiredBorderColor
import com.abone.abonex.ui.theme.poppins

val bottomNavItems = listOf(
    BottomNavItem(
        label = "Anasayfa",
        icon = Icons.Default.Home,
        route = "home"
    ),
    BottomNavItem(
        label = "Ayar",
        icon = Icons.Default.Settings,
        route = "settings"
    ),
    BottomNavItem(
        label = "Profil",
        icon = Icons.Default.Person,
        route = "profile"
    ),
    BottomNavItem(
        label = "Çıkış",
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
    BottomAppBar(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .height(70.dp)
            .clip(RoundedCornerShape(24.dp)),
        containerColor = Color(0xFF1F1F2E),
        tonalElevation = 8.dp,
        contentPadding = PaddingValues(horizontal = 5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomNavigationItem(
                    item = bottomNavItems[0],
                    isSelected = currentRoute == bottomNavItems[0].route,
                    onClick = { onItemClick(bottomNavItems[0]) }
                )
                CustomNavigationItem(
                    item = bottomNavItems[1],
                    isSelected = currentRoute == bottomNavItems[1].route,
                    onClick = { onItemClick(bottomNavItems[1]) }
                )

                IconButton(
                    onClick = onFabClick,
                    modifier = Modifier
                        .size(56.dp)
                        .background(color = expiredBorderColor, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Ekle",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                CustomNavigationItem(
                    item = bottomNavItems[2],
                    isSelected = currentRoute == bottomNavItems[2].route,
                    onClick = { onItemClick(bottomNavItems[2]) }
                )
                CustomNavigationItem(
                    item = bottomNavItems[3],
                    isSelected = currentRoute == bottomNavItems[3].route,
                    onClick = { onItemClick(bottomNavItems[3]) }
                )
            }
        }
    }
}
@Composable
private fun CustomNavigationItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
        label = "content color animation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable(onClick = onClick).padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = contentColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.label,
            color = contentColor,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}