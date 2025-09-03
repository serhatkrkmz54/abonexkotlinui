package com.abone.abonex.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abone.abonex.components.main.SubscriptionLogo
import com.abone.abonex.domain.model.PaymentHistory
import com.abone.abonex.domain.model.Subscription
import com.abone.abonex.navigation.AppRoute
import com.abone.abonex.ui.features.SubscriptionDetailViewModel
import com.abone.abonex.ui.theme.GradientEnd
import com.abone.abonex.ui.theme.GradientStart
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionDetailScreen(
    navController: NavController,
    viewModel: SubscriptionDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(state.cancellationSuccess) {
        if (state.cancellationSuccess) {
            navController.popBackStack(AppRoute.HOME_SCREEN, inclusive = false)
        }
    }

    // Onay Diyaloğu
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Aboneliği İptal Et") },
            text = { Text("Bu aboneliği iptal etmek istediğinizden emin misiniz? Bu işlem geri alınamaz.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.cancelSubscription()
                        showConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Evet, İptal Et")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Vazgeç")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.subscription?.name ?: "Detaylar") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Geri")
                    }
                },
                // TopAppBar'a aksiyon menüsü ekliyoruz
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Daha Fazla Seçenek")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("İptal Et", color = MaterialTheme.colorScheme.error) },
                                onClick = {
                                    showMenu = false
                                    showConfirmDialog = true // Onay diyaloğunu göster
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "İptal Et",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            )
                            // TODO: "Düzenle" gibi başka seçenekler buraya eklenebilir
                        }
                    }
                }
            )
        },
    ) { padding ->
        when {
            state.isLoading -> Box(
                Modifier.fillMaxSize().padding(padding),
                Alignment.Center
            ) { CircularProgressIndicator() }

            state.error != null -> Box(
                Modifier.fillMaxSize().padding(padding),
                Alignment.Center
            ) { Text("Hata: ${state.error}") }

            state.subscription != null -> {
                val sub = state.subscription!!
                LazyColumn(
                    modifier = Modifier.padding(padding),
                    contentPadding = PaddingValues(all = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item { DetailHeader(sub) }

                    item { DetailInfoCard(sub) }

                    if (state.paymentHistory.isNotEmpty()) {
                        item { HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)) }

                        item {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = "Ödeme Geçmişi",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Ödeme Geçmişi",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        items(state.paymentHistory) { historyItem ->
                            PaymentHistoryItem(historyItem)
                        }
                    } else {
                        item {
                            Text("Henüz ödeme kaydı bulunmuyor.", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun DetailHeader(sub: Subscription) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        SubscriptionLogo(
            subscriptionName = sub.name,
            logoUrl = sub.logoUrl
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(sub.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                "${String.format("%.2f", sub.amount)} ${getCurrencySymbol(sub.currency)} ${formatBillingCycle(sub.billingCycle)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun DetailInfoCard(sub: Subscription) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

    val gradientBrush = Brush.linearGradient(
        colors = listOf(GradientStart, GradientEnd),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(brush = gradientBrush)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            DetailInfoRow(
                icon = Icons.Default.DateRange,
                label = "Başlangıç Tarihi",
                value = LocalDate.parse(sub.startDate).format(dateFormatter),
                contentColor = Color.White // 3. İçerik rengini beyaz yapıyoruz.
            )
            DetailInfoRow(
                icon = Icons.Default.Today,
                label = "Sonraki Ödeme",
                value = LocalDate.parse(sub.nextPaymentDate).format(dateFormatter),
                contentColor = Color.White // 3. İçerik rengini beyaz yapıyoruz.
            )
            sub.endDate?.let {
                DetailInfoRow(
                    icon = Icons.Default.EventBusy,
                    label = "Bitiş Tarihi",
                    value = LocalDate.parse(it).format(dateFormatter),
                    contentColor = Color.White // 3. İçerik rengini beyaz yapıyoruz.
                )
            }
            if (!sub.cardName.isNullOrBlank()) {
                DetailInfoRow(
                    icon = Icons.Default.CreditCard,
                    label = "Kart",
                    value = "${sub.cardName} •••• ${sub.cardLastFourDigits}",
                    contentColor = Color.White // 3. İçerik rengini beyaz yapıyoruz.
                )
            }
        }
    }
}


@Composable
private fun DetailInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    contentColor: Color = MaterialTheme.colorScheme.onSurface // Varsayılan renk
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor, // Rengi parametreden al
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = contentColor.copy(alpha = 0.7f)) // Etiketi biraz soluk yap
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = contentColor)
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun PaymentHistoryItem(history: PaymentHistory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                history.paymentDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                "${String.format("%.2f", history.amountPaid)} ${getCurrencySymbol(history.currency)}",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SubscriptionLogo(subscriptionName: String) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = getIconForManualSubscription(subscriptionName),
            contentDescription = subscriptionName,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
    }
}

private fun getIconForManualSubscription(name: String): ImageVector {
    val lowerCaseName = name.lowercase(Locale.getDefault())
    return when {
        "fatura" in lowerCaseName || "fatur" in lowerCaseName -> Icons.Filled.FilePresent
        "internet" in lowerCaseName || "inter" in lowerCaseName -> Icons.Default.Wifi
        "telefon" in lowerCaseName || "gsm" in lowerCaseName -> Icons.Default.PhoneAndroid
        "kira" in lowerCaseName || "kira" in lowerCaseName -> Icons.Default.Home
        "gym" in lowerCaseName || "spor" in lowerCaseName -> Icons.Default.FitnessCenter
        "müzik" in lowerCaseName || "music" in lowerCaseName -> Icons.Default.MusicNote
        "aidat" in lowerCaseName -> Icons.Default.Business
        else -> Icons.AutoMirrored.Default.HelpOutline
    }
}

private fun getCurrencySymbol(currency: String): String {
    return when (currency.uppercase()) { "TRY" -> "₺"; "USD" -> "$"; "EUR" -> "€"; else -> currency }
}

private fun formatBillingCycle(cycle: String): String {
    return when (cycle.uppercase()) { "MONTHLY" -> "/Aylık"; "YEARLY" -> "/Yıllık"; else -> "" }
}