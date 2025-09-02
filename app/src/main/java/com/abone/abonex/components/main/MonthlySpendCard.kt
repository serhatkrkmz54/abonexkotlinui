package com.abone.abonex.components.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abone.abonex.R
import com.abone.abonex.ui.theme.GradientEnd
import com.abone.abonex.ui.theme.GradientStart
import java.time.Month
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun MonthlySpendCard(
    amount: Double,
    currency: String,
    month: String,
    modifier: Modifier = Modifier,
    cardTypeLogo: Int? = null
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(GradientStart, GradientEnd),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(brush = gradientBrush)
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_turkish_lira),
                contentDescription = "Toplam Harcama İkonu",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${String.format("%.2f", amount)} $currency",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${formatMonthName(month)} Ayı Toplamı",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            cardTypeLogo?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = "Kart Tipi Logosu",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

private fun formatMonthName(month: String): String {
    return when (month.uppercase(Locale.ROOT)) {
        "JANUARY" -> "Ocak"
        "FEBRUARY" -> "Şubat"
        "MARCH" -> "Mart"
        "APRIL" -> "Nisan"
        "MAY" -> "Mayıs"
        "JUNE" -> "Haziran"
        "JULY" -> "Temmuz"
        "AUGUST" -> "Ağustos"
        "SEPTEMBER" -> "Eylül"
        "OCTOBER" -> "Ekim"
        "NOVEMBER" -> "Kasım"
        "DECEMBER" -> "Aralık"
        else -> month.lowercase(Locale.ROOT).replaceFirstChar { it.titlecase(Locale.ROOT) }
    }
}