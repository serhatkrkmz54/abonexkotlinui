package com.abone.abonex.components.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.abone.abonex.domain.model.Subscription
import com.abone.abonex.navigation.AppRoute

@Composable
fun SubscriptionCategorySection(
    navController: NavController,
    title: String,
    subscriptions: List<Subscription>,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    icon: ImageVector,
    itemBorderColor: Color = Color.Transparent
) {
    if (subscriptions.isNotEmpty()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = titleColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = titleColor
                )
            }
            subscriptions.forEach { subscription ->
                SubscriptionItem(subscription = subscription,borderColor = itemBorderColor,
                    onClick = {
                    navController.navigate("${AppRoute.SUBSCRIPTION_DETAIL_SCREEN}/${subscription.id}")
                })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}