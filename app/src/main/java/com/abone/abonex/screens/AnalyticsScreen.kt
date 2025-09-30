package com.abone.abonex.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.PlaylistAddCheck
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abone.abonex.domain.model.CardSpending
import com.abone.abonex.ui.features.AnalyticsViewModel
import java.time.format.DateTimeFormatter

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    navController: NavController,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analiz ve İstatistikler") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Geri")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading && state.summary == null -> CircularProgressIndicator()
                state.error != null -> Text("Hata: ${state.error}")
                state.summary != null -> {
                    val summary = state.summary!!
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        item {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.height(350.dp),
                                userScrollEnabled = false
                            ) {
                                item {
                                    KpiCard(
                                        icon = Icons.Default.AccountBalanceWallet,
                                        label = "Aylık Toplam Gider",
                                        value = "${String.format("%.2f", summary.totalMonthlyCost)} ₺",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                item {
                                    KpiCard(
                                        icon = Icons.Default.PlaylistAddCheck,
                                        label = "Aktif Abonelik",
                                        value = summary.activeSubscriptionCount.toString(),
                                        color = Color(0xFF66BB6A)
                                    )
                                }
                                item {
                                    KpiCard(
                                        icon = Icons.Default.TrendingUp,
                                        label = "En Yüksek Gider",
                                        value = summary.mostExpensiveSubscription?.name ?: "-",
                                        subValue = summary.mostExpensiveSubscription?.let { "${String.format("%.2f", it.monthlyCost)} ₺/Aylık" },
                                        color = Color(0xFFE53935)
                                    )
                                }
                                item {
                                    KpiCard(
                                        icon = Icons.Default.Update,
                                        label = "Sıradaki Büyük Ödeme",
                                        value = summary.nextBigPayment?.name ?: "-",
                                        subValue = summary.nextBigPayment?.let {
                                            "${it.nextPaymentDate.format(DateTimeFormatter.ofPattern("dd MMM"))} → ${String.format("%.2f", it.amount)} ₺"
                                        },
                                        color = Color(0xFFFFA726)
                                    )
                                }
                            }
                        }

                        if (state.spendingByCard.isNotEmpty()) {
                            item {
                                Text(
                                    "Kart Harcama Dağılımı",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            items(state.spendingByCard) { cardSpending ->
                                CardSpendingItem(cardSpending = cardSpending)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KpiCard(
    icon: ImageVector,
    label: String,
    value: String,
    subValue: String? = null,
    color: Color
) {
    Card(
        modifier = Modifier.aspectRatio(1f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                Text(label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                subValue?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun CardSpendingItem(cardSpending: CardSpending) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = "Kart",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(cardSpending.cardName, fontWeight = FontWeight.Bold)
                Text("•••• ${cardSpending.cardLastFourDigits}", color = Color.Gray, fontSize = 12.sp)
            }
            Text(
                "${String.format("%.2f", cardSpending.totalAmount)} ₺",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}