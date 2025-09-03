package com.abone.abonex.components.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.abone.abonex.R
import com.abone.abonex.di.NetworkModule.BASE_URL
import com.abone.abonex.domain.model.Subscription
import com.abone.abonex.ui.theme.redRose
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun SubscriptionItem(
    subscription: Subscription,
    modifier: Modifier = Modifier,
    borderColor: Color = Color.Transparent,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(
                width = 0.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(15 .dp)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F2E))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubscriptionLogo(subscriptionName = subscription.name, logoUrl = subscription.logoUrl,)

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = subscription.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = formatDueDate(subscription.nextPaymentDate), fontSize = 14.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatDateRange(subscription.startDate, subscription.endDate),
                    fontSize = 12.sp,
                    color = Color.Gray.copy(alpha = 0.7f)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${String.format("%.2f", subscription.amount)} ${getCurrencySymbol(subscription.currency)}",
                    fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatBillingCycle(subscription.billingCycle),
                    fontSize = 14.sp, color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun SubscriptionLogo(logoUrl: String?, subscriptionName: String) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF101018)),
        contentAlignment = Alignment.Center
    ) {
        if (!logoUrl.isNullOrBlank()) {
            AsyncImage(
                model = BASE_URL + logoUrl,
                contentDescription = subscriptionName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.default_brand),
                error = painterResource(id = R.drawable.ic_error_image)
            )
        } else {
            // EĞER logoUrl YOKSA (Manuel Abonelik): İsme göre yerel bir ikon seç
            Icon(
                imageVector = getIconForManualSubscription(subscriptionName),
                contentDescription = subscriptionName,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
private fun getIconForManualSubscription(name: String): ImageVector {
    val lowerCaseName = name.lowercase(Locale.getDefault())
    return when {
        "fatura" in lowerCaseName || "fatur" in lowerCaseName -> Icons.Filled.FilePresent
        "internet" in lowerCaseName || "inter" in lowerCaseName -> Icons.Filled.Wifi
        "telefon" in lowerCaseName || "telef" in lowerCaseName -> Icons.Filled.PhoneAndroid
        "kira" in lowerCaseName || "kira" in lowerCaseName -> Icons.Default.Home
        "gym" in lowerCaseName || "spor" in lowerCaseName -> Icons.Default.FitnessCenter
        "müzik" in lowerCaseName || "muzik" in lowerCaseName -> Icons.Default.MusicNote
        "aidat" in lowerCaseName -> Icons.Default.Business
        else -> Icons.AutoMirrored.Default.HelpOutline
    }
}

private fun formatDueDate(dateString: String): String {
    return try {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val nextPaymentDate = LocalDate.parse(dateString, formatter)
        val today = LocalDate.now()
        val daysBetween = ChronoUnit.DAYS.between(today, nextPaymentDate)

        when {
            daysBetween < 0 -> "Süresi Doldu"
            daysBetween == 0L -> "Bugün Son"
            daysBetween == 1L -> "Yarın Son"
            else -> "Son $daysBetween gün"
        }
    } catch (e: Exception) {
        "Tarih Belirsiz"
    }
}

private fun formatDateRange(startDateStr: String, endDateStr: String?): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return try {
        val startDate = LocalDate.parse(startDateStr).format(formatter)
        val endDate = endDateStr?.let { LocalDate.parse(it).format(formatter) }

        if (endDate != null) {
            "$startDate - $endDate"
        } else {
            "Başlangıç: $startDate"
        }
    } catch (e: Exception) {
        ""
    }
}

private fun getCurrencySymbol(currency: String): String {
    return when (currency.uppercase()) {
        "TRY" -> "₺"
        "USD" -> "$"
        "EUR" -> "€"
        else -> currency
    }
}

private fun formatBillingCycle(cycle: String): String {
    return when (cycle.uppercase()) {
        "MONTHLY" -> "/Aylık"
        "YEARLY" -> "/Yıllık"
        else -> ""
    }
}